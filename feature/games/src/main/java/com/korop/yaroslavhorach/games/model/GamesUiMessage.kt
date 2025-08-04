package com.korop.yaroslavhorach.games.model

sealed class GamesUiMessage {
    class NavigateToGameDescription(val gameId: Long, val useToken: Boolean) : GamesUiMessage()
}