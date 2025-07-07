package com.korop.yaroslavhorach.designsystem.screens.premium_success.model

sealed class PremiumSuccessAction {
    data object OnPrimaryBtnClicked : PremiumSuccessAction()
}