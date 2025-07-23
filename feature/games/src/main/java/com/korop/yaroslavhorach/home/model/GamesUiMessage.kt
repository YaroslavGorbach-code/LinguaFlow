package com.korop.yaroslavhorach.home.model

import com.korop.yaroslavhorach.domain.game.model.Game

sealed class GamesUiMessage {
    class NavigateToGame(val gameId: Long, val gameName: Game.GameName) : GamesUiMessage()
    class ScrollToAndShowDescription(val gameId: Long): GamesUiMessage()
}