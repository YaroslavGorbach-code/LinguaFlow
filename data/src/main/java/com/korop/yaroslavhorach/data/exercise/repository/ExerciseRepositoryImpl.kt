package com.korop.yaroslavhorach.data.exercise.repository

import android.content.Context
import androidx.compose.ui.util.fastFilter
import com.korop.yaroslavhorach.common.utill.loadJsonFromAssets
import com.korop.yaroslavhorach.database.dao.ExerciseProgressDao
import com.korop.yaroslavhorach.database.task.model.asDomainModel
import com.korop.yaroslavhorach.database.task.model.asEntityModel
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.korop.yaroslavhorach.datastore.prefs.LinguaPrefsDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseProgressDao: ExerciseProgressDao,
    private val prefsDataSource: LinguaPrefsDataSource,
    @ApplicationContext private val context: Context
) : ExerciseRepository {

    private var currentBLock: MutableStateFlow<ExerciseBlock> = MutableStateFlow(ExerciseBlock.ONE)

    override fun getExercises(): Flow<List<Exercise>> {
        return exerciseProgressDao.getExerciseProgressEntities()
            .flatMapLatest { progressList ->
                val progressMap = progressList.associateBy { it.exerciseId }
                val exercises = getRawExercises().fastFilter { it.isVisible }

                flowOf(
                    exercises.mapIndexed { index, exercise ->
                        val progress = progressMap[exercise.id]

                        val lastActive = exercises
                            .indexOfLast { ex -> (progressMap[ex.id]?.progress ?: 0) > 0 }
                            .takeIf { it != -1 } ?: 0

                        if (progress != null) {
                            exercise.copy(exerciseProgress = progress.asDomainModel(), isEnable = true, isLastActive = lastActive == index)
                        } else {
                            val previousExerciseIsFinished = if (index == 0){
                                true
                            } else {
                                progressMap[exercises[index.dec()].id]?.isFinished == true
                            }

                            exercise.copy(isEnable = previousExerciseIsFinished, isLastActive = lastActive == index)
                        }
                    }
                )
            }
    }

    override suspend fun getExercise(exerciseId: Long): Exercise? {
        val progress = exerciseProgressDao.getExerciseProgressEntity(exerciseId)
        val exercise = getRawExercises().firstOrNull { it.id == exerciseId }

        if (progress == null && exercise != null) {
            exerciseProgressDao.upsertExerciseProgress(exercise.exerciseProgress.asEntityModel())
        }

        return exercise?.copy(
            exerciseProgress = progress?.asDomainModel() ?: exercise.exerciseProgress,
            isEnable = true
        )
    }

    override suspend fun markCompleted(exerciseId: Long) {
        val progress = exerciseProgressDao.getExerciseProgressEntity(exerciseId)

        progress?.copy(progress = progress.progress.inc())
            ?.let { exerciseProgressDao.upsertExerciseProgress(it) }
    }

    override suspend fun changeBlock(exerciseBlock: ExerciseBlock) {
        currentBLock.emit(exerciseBlock)
    }

    override fun getBlock(): Flow<ExerciseBlock> {
        return currentBLock
    }

    override suspend fun addStar(name: ExerciseBlock) {
        prefsDataSource.addStar(name)
    }

    override fun getStarsForBlock(name: ExerciseBlock): Flow<Int> {
        return prefsDataSource.getStarsForExercise(name)
    }

    private fun getRawExercises(): List<Exercise> {
        return (loadJsonFromAssets(context, "exercises.json")?.let { exercises ->
            Gson().fromJson(exercises, object : TypeToken<List<Exercise>>() {}.type)
        } ?: emptyList<Exercise>())
    }
}