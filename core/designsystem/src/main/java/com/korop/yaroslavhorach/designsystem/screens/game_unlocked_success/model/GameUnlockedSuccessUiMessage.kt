package com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model

sealed class GameUnlockedSuccessUiMessage{
    data class NavigateToGame(val id: Long): GameUnlockedSuccessUiMessage()
    data object NavigateBack : GameUnlockedSuccessUiMessage()
}