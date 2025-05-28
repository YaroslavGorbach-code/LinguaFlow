package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.common.utill.UiMessage

data class GamesViewState(
    val userName: String = "",
    val uiMessage: UiMessage<GamesUiMessage>? = null
) {

    companion object {
        val Empty = GamesViewState()
        val Preview = GamesViewState()
    }
}
