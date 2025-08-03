package com.korop.yaroslavhorach.games.model

sealed class GamesAction {
    data object OnStartDailyChallengeClicked : GamesAction()
    data object OnGoToDailyChallengeExercises : GamesAction()
    data class OnGameClicked(val gameUi: GameUi) : GamesAction()
    data class OnStartGameClicked(val gameUi: GameUi, val useToken: Boolean) : GamesAction()
    class OnSortSelected(val item: GameSort) : GamesAction()
    class OnAddToFavoritesClicked(val game: GameUi) : GamesAction()
    class OnRemoveFavoritesClicked(val game: GameUi) : GamesAction()
    data object OnPremiumBtnClicked : GamesAction()
}