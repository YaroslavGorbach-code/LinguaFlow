package com.korop.yaroslavhorach.home.model

import com.korop.yaroslavhorach.domain.game.model.Game

sealed class GamesUiMessage {
    class NavigateToExercise(val gameId: Long, val gameName: Game.GameName) : GamesUiMessage()
}