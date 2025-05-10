package com.example.yaroslavhorach.exercises.speaking.model

sealed class SpeakingExerciseUiMessage {
    data object RequestRecordAudio: SpeakingExerciseUiMessage()
    data class ShowWrongAnswerExplanation(val text: String): SpeakingExerciseUiMessage()
    data class ShowCorrectAnswerExplanation(val text: String): SpeakingExerciseUiMessage()
}