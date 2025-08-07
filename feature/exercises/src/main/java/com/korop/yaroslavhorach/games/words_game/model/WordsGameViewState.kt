package com.korop.yaroslavhorach.games.words_game.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.domain.game.model.Game

data class WordsGameViewState(
    val game: Game? = null,
    val block: ExerciseBlock = ExerciseBlock.ONE,
    val progress: Float = 0f,
    val isLastExercise: Boolean = false,
    val screenMode: ScreenMode = ScreenMode.Words(emptyList()),
    val uiMessage: UiMessage<WordsGameUiMessage>? = null
) {

    sealed class ScreenMode {
        data class Words(val words: List<String>) : ScreenMode()
        data class Sentence(val sentence: String) : ScreenMode()
    }

    companion object {
        val Empty = WordsGameViewState()
        val PreviewSpeaking = WordsGameViewState(screenMode = ScreenMode.Words(listOf("Alo", "Eto")))
    }
}