package com.korop.yaroslavhorach.designsystem.screens.rate.model

import com.korop.yaroslavhorach.common.utill.UiMessage

data class RateAppViewState(
    val uiMessage: UiMessage<RateAppUiMessage>? = null
) {

    companion object {
        val Empty = RateAppViewState()
        val Preview = RateAppViewState()
    }
}
