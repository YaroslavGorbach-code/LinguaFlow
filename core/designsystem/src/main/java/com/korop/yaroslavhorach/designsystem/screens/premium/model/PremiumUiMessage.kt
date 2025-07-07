package com.korop.yaroslavhorach.designsystem.screens.premium.model

sealed class PremiumUiMessage {
    data object NavigateToSuccess : PremiumUiMessage()
}