package com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.game.model.Game

data class GameUnlockedSuccessViewState(
    val game: Game? = null,
    val isPremium: Boolean = false,
    val uiMessage: UiMessage<GameUnlockedSuccessUiMessage>? = null
) {

    companion object {
        val Empty = GameUnlockedSuccessViewState()
        val Preview = GameUnlockedSuccessViewState()
    }
}
