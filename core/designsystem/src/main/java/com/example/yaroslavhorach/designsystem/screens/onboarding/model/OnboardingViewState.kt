package com.example.yaroslavhorach.designsystem.screens.onboarding.model

import com.example.yaroslavhorach.common.utill.UiMessage

data class OnboardingViewState(
    val uiMessage: UiMessage<OnboardingUiMessage>? = null
) {

    companion object {
        val Empty = OnboardingViewState()
        val Preview = OnboardingViewState()
    }
}
