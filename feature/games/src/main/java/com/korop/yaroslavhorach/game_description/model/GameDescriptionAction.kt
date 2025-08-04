package com.korop.yaroslavhorach.game_description.model

sealed class GameDescriptionAction {
    data object OnRemoveFavoritesClicked : GameDescriptionAction()
    data object OnAddToFavoritesClicked : GameDescriptionAction()
    data object OnStartGameClicked : GameDescriptionAction()
    data object OnBackClicked : GameDescriptionAction()
    data object OnPremiumBtnClicked: GameDescriptionAction()
}