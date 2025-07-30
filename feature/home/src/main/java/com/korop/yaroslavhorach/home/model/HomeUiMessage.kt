package com.korop.yaroslavhorach.home.model

sealed class HomeUiMessage {
    data class ScrollTo(val index: Int) : HomeUiMessage()
}