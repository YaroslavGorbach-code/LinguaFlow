package com.example.yaroslavhorach.domain.exercise_content

import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.domain.exercise_content.model.TongueTwister

interface ExerciseContentRepository {
    suspend fun getSituation(exerciseName: ExerciseName): Situation
    suspend fun getTongueTwister(difficulty: TongueTwister.Difficulty): TongueTwister
    suspend fun getTests(exerciseName: ExerciseName): List<Test>
}