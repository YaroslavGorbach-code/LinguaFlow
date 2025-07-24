package com.korop.yaroslavhorach.exercises.exercise_completed.model

sealed class ExerciseCompletedUiMessage {
    data object ShowAd : ExerciseCompletedUiMessage()
    data object NavigateToRateApp : ExerciseCompletedUiMessage()
    class NavigateToGameUnlocked(val gameId: Long): ExerciseCompletedUiMessage()
}