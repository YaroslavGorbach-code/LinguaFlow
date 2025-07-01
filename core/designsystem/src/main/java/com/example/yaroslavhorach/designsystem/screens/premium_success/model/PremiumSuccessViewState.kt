package com.example.yaroslavhorach.designsystem.screens.premium_success.model

import com.example.yaroslavhorach.common.utill.UiMessage

data class PremiumSuccessViewState(
    val uiMessage: UiMessage<PremiumSuccessUiMessage>? = null
) {

    companion object {
        val Empty = PremiumSuccessViewState()
        val Preview = PremiumSuccessViewState()
    }
}
