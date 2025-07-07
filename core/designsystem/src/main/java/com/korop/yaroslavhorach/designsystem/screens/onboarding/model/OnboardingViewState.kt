package com.korop.yaroslavhorach.designsystem.screens.onboarding.model

import com.korop.yaroslavhorach.common.utill.UiMessage

data class OnboardingViewState(
    val uiMessage: UiMessage<OnboardingUiMessage>? = null
) {

    companion object {
        val Empty = OnboardingViewState()
        val Preview = OnboardingViewState()
    }
}
