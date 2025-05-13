package com.example.yaroslavhorach.exercises.speaking.model

sealed class SpeakingExerciseUiMessage {
    data object RequestRecordAudio: SpeakingExerciseUiMessage()
    data object NavigateToExerciseResult: SpeakingExerciseUiMessage()
    data class ShowWrongAnswerExplanation(val text: String): SpeakingExerciseUiMessage()
    data class ShowCorrectAnswerExplanation(val text: String): SpeakingExerciseUiMessage()
}