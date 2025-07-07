package com.korop.yaroslavhorach.exercises.tongue_twisters.model

sealed class TongueTwisterExerciseUiMessage {
    data class NavigateToExerciseResult(val time: Long, val experience: Int) : TongueTwisterExerciseUiMessage()
}