package com.example.yaroslavhorach.designsystem.screens.premium.model

sealed class PremiumUiMessage {
    data object NavigateToSuccess : PremiumUiMessage()
}