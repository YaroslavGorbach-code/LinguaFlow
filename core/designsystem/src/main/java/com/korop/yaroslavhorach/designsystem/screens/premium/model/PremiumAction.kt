package com.korop.yaroslavhorach.designsystem.screens.premium.model

sealed class PremiumAction {
    data class OnVariantChosen(val premiumVariant: PremiumVariant) : PremiumAction()
    data object OnBack : PremiumAction()
    data object OnGetPremiumClicked : PremiumAction()
}