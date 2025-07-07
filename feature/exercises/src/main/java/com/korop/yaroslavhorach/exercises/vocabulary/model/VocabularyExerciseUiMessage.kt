package com.korop.yaroslavhorach.exercises.vocabulary.model

sealed class VocabularyExerciseUiMessage {
    data class NavigateToExerciseResult(val time: Long, val experience: Int): VocabularyExerciseUiMessage()
    data class ShowBadResult(val text: String, val amountWords: Int): VocabularyExerciseUiMessage()
    data class ShowNotBadResult(val text: String, val amountWords: Int): VocabularyExerciseUiMessage()
    data class ShowCorrectResult(val text: String, val amountWords: Int): VocabularyExerciseUiMessage()
}