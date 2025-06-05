package com.example.yaroslavhorach.games.words_game.model

sealed class WordsGameAction {
    data object OnNextClicked : WordsGameAction()
    data object OnBackClicked: WordsGameAction()
}