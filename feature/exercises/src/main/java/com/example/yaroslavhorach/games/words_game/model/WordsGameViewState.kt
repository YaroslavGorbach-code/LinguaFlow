package com.example.yaroslavhorach.games.words_game.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.game.model.Game

data class WordsGameViewState(
    val game: Game? = null,
    val words: List<String> = emptyList(),
    val block: ExerciseBlock = ExerciseBlock.ONE,
    val progress: Float = 0f,
    val isLastExercise: Boolean = false,
    val uiMessage: UiMessage<WordsGameUiMessage>? = null
) {

    companion object {
        val Empty = WordsGameViewState()
        val PreviewSpeaking = WordsGameViewState(words = listOf("Alo", "Eto"))

    }
}