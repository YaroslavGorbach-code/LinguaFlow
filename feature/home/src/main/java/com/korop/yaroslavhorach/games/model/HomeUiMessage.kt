package com.korop.yaroslavhorach.games.model

sealed class HomeUiMessage {
    data class ScrollTo(val index: Int) : HomeUiMessage()
    data object NavigateToBlockIsLocked : HomeUiMessage()
}