package com.korop.yaroslavhorach.exercises.exercise_completed.model

sealed class ExerciseCompletedAction {
    data object OnContinueClicked : ExerciseCompletedAction()
}