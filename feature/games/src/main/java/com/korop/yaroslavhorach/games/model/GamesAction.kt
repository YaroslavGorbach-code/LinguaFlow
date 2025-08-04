package com.korop.yaroslavhorach.games.model

import com.korop.yaroslavhorach.game_description.model.GameUi

sealed class GamesAction {
    data object OnStartDailyChallengeClicked : GamesAction()
    data object OnGoToDailyChallengeExercises : GamesAction()
    data class OnGameClicked(val gameUi: GameUi) : GamesAction()
    class OnSortSelected(val item: GameSort) : GamesAction()
}