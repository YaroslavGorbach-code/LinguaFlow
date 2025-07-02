package com.example.yaroslavhorach.designsystem.screens.onboarding.model

sealed class OnboardingAction {
    data object OnStartClicked : OnboardingAction()
}