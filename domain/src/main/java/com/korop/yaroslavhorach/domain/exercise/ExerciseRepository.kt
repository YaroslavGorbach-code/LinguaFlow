package com.korop.yaroslavhorach.domain.exercise

import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
     fun getExercises(): Flow<List<Exercise>>
     suspend fun getExercise(exerciseId: Long): Exercise?
     suspend fun markCompleted(exerciseId: Long)
     suspend fun changeBlock(exerciseBlock: ExerciseBlock)
     fun getBlock() : Flow<ExerciseBlock>
}