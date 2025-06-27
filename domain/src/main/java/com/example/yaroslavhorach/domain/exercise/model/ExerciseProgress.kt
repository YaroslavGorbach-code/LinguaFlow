package com.example.yaroslavhorach.domain.exercise.model

data class ExerciseProgress(val exerciseId: Long, val progress: Int, val maxProgress: Int) {
    val isFinished: Boolean
        get() = progress >= maxProgress
}