package com.korop.yaroslavhorach.designsystem.screens.premium.model

sealed class PremiumUiMessage {
    class NavigateToSuccess : PremiumUiMessage()
    data object ShowLifeTimeSubscriptionDialog : PremiumUiMessage()
    data object ShowMonthSubscriptionDialog : PremiumUiMessage()
    data object Show6MonthSubscriptionDialog : PremiumUiMessage()
}