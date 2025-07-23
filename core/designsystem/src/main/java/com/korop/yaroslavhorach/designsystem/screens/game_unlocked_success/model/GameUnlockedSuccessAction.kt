package com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model

sealed class GameUnlockedSuccessAction {
    data class OnTryGameBtnClicked(val id: Long) : GameUnlockedSuccessAction()
    data object OnContinueBtnClicked : GameUnlockedSuccessAction()
}