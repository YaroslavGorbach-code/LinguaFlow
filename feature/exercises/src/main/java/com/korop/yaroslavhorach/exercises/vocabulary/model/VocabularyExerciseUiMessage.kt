package com.korop.yaroslavhorach.exercises.vocabulary.model

import com.korop.yaroslavhorach.domain.game.model.Game

sealed class VocabularyExerciseUiMessage {
    data class NavigateToExerciseResult(val time: Long, val experience: Int, val gameName: Game.GameName): VocabularyExerciseUiMessage()
    data class ShowBadResult(val text: String, val amountWords: Int): VocabularyExerciseUiMessage()
    data class ShowNotBadResult(val text: String, val amountWords: Int): VocabularyExerciseUiMessage()
    data class ShowCorrectResult(val text: String, val amountWords: Int): VocabularyExerciseUiMessage()
}