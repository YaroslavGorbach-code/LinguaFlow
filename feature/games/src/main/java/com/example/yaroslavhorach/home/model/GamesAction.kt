package com.example.yaroslavhorach.home.model

sealed class GamesAction {
    data object OnStartDailyChallengeClicked : GamesAction()
    data class OnGameClicked(val gameUi: GameUi) : GamesAction()
    data class OnStartGameClicked(val gameUi: GameUi) : GamesAction()
    data object OnPremiumBtnClicked : GamesAction()
}