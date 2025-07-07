package com.korop.yaroslavhorach.designsystem.screens.onboarding.model

sealed class OnboardingAction {
    data object OnStartClicked : OnboardingAction()
}