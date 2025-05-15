package com.example.yaroslavhorach.data.exercise.repository

import android.content.Context
import com.example.yaroslavhorach.common.utill.loadJsonFromAssets
import com.example.yaroslavhorach.database.dao.ExerciseProgressDao
import com.example.yaroslavhorach.database.task.model.asDomainModel
import com.example.yaroslavhorach.database.task.model.asEntityModel
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseProgressDao: ExerciseProgressDao,
    @ApplicationContext private val context: Context
) : ExerciseRepository {

    override fun getExercises(): Flow<List<Exercise>> {
        return exerciseProgressDao.getExerciseProgressEntities()
            .flatMapLatest { progressList ->
                val progressMap = progressList.associateBy { it.exerciseId }
                val exercises = getRawExercises()

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
        val exercise = getRawExercises().find { it.id == exerciseId }

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

    private fun getRawExercises(): List<Exercise> {
        return loadJsonFromAssets(context, "exercises.json")?.let { exercises ->
            Gson().fromJson(exercises, object : TypeToken<List<Exercise>>() {}.type)
        } ?: emptyList()
    }
}