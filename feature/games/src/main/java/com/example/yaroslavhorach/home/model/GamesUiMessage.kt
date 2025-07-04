package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.domain.game.model.Game

sealed class GamesUiMessage {
    class NavigateToExercise(val gameId: Long, val gameName: Game.GameName) : GamesUiMessage()
}