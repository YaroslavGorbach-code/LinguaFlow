package com.korop.yaroslavhorach.games.words_game.model

sealed class WordsGameAction {
    data object OnNextClicked : WordsGameAction()
    data object OnBackClicked: WordsGameAction()
}