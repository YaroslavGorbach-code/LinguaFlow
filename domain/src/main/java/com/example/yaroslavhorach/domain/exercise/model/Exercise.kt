package com.example.yaroslavhorach.domain.exercise.model

data class Exercise(
    val id: Long = 0,
    val exerciseProgress: ExerciseProgress,
    val exerciseName: ExerciseName,
    val skill: Skill,
    val block: ExerciseBlock,
    val isEnable: Boolean = true,
    val isLastActive: Boolean = false
)