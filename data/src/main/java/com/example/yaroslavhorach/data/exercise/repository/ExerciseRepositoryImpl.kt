package com.example.yaroslavhorach.data.exercise.repository

import com.example.yaroslavhorach.database.dao.ExerciseProgressDao
import com.example.yaroslavhorach.database.task.model.asDomainModel
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise.model.ExerciseProgress
import com.example.yaroslavhorach.domain.exercise.model.Skill
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val exerciseProgressDao: ExerciseProgressDao) : ExerciseRepository {

    override fun getExercises(): Flow<List<Exercise>> {
        return exerciseProgressDao.getExerciseProgressEntities().flatMapLatest { progressList ->
            val progressMap = progressList.associateBy { it.exerciseId }
            val exercises = getRawExercises()
            val lastFinishedIndex = exercises.indexOfLast { exercise ->
                val progress = progressMap[exercise.id]?.progress

                progress != null && progress > 0
            }

            val lasrtActive = exercises.indexOfLast {
                it.exerciseProgress.progress > 0
            }.takeIf  { it != -1 } ?: 0

            flowOf(
                exercises.mapIndexed  { index, exercise ->
                    val progress = progressMap[exercise.id]

                    if (progress != null) {
                        exercise.copy(exerciseProgress = progress.asDomainModel(), isEnable = true)
                    } else {
                        exercise.copy(isEnable = lasrtActive == index, isLastActive = lasrtActive == index)
                    }
                }
            )
        }
    }

    private fun getRawExercises(): List<Exercise> {
        return listOf(
            Exercise(
                id = 1,
                exerciseName = ExerciseName.ICEBREAKERS,
                skill = Skill.COMMUNICATION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
                block = ExerciseBlock.ONE
            ),
            Exercise(
                id = 2,
                exerciseName = ExerciseName.FINISH_THE_THOUGHT,
                skill = Skill.COMMUNICATION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
                block = ExerciseBlock.ONE
            ),
            Exercise(
                id = 3,
                exerciseName = ExerciseName.TONGUE_TWISTERS_EASY,
                skill = Skill.DICTION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 2),
                block = ExerciseBlock.ONE
            ),
            Exercise(
                id = 4,
                exerciseName = ExerciseName.THE_KEY_TO_SMALL_TALK,
                skill = Skill.COMMUNICATION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
                block = ExerciseBlock.ONE
            ),
            Exercise(
                id = 5,
                exerciseName = ExerciseName.DATING_ROUTE,
                skill = Skill.COMMUNICATION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
                block = ExerciseBlock.ONE
            ),
            Exercise(
                id = 6,
                exerciseName = ExerciseName.VOCABULARY,
                skill = Skill.VOCABULARY,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 1),
                block = ExerciseBlock.ONE
            ),
            Exercise(
                id = 7,
                exerciseName = ExerciseName.FAREWELL_REMARK,
                skill = Skill.COMMUNICATION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
                block = ExerciseBlock.ONE
            )
        )
    }
}