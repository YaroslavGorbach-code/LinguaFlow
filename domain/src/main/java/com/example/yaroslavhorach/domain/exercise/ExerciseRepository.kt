package com.example.yaroslavhorach.domain.exercise

import com.example.yaroslavhorach.domain.exercise.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
     fun getExercises(): Flow<List<Exercise>>
}