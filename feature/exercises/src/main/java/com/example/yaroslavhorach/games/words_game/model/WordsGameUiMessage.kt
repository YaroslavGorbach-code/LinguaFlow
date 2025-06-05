package com.example.yaroslavhorach.games.words_game.model

sealed class WordsGameUiMessage {
    data class NavigateToExerciseResult(val time: Long, val experience: Int) : WordsGameUiMessage()
}