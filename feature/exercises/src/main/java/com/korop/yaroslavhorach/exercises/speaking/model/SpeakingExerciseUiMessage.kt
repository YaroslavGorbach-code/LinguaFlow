package com.korop.yaroslavhorach.exercises.speaking.model

sealed class SpeakingExerciseUiMessage {
    data object RequestRecordAudio: SpeakingExerciseUiMessage()
    data class NavigateToExerciseResult(val time: Long, val experience: Int): SpeakingExerciseUiMessage()
    data class ShowWrongAnswerExplanation(val text: String): SpeakingExerciseUiMessage()
    data class ShowCorrectAnswerExplanation(val text: String): SpeakingExerciseUiMessage()
}