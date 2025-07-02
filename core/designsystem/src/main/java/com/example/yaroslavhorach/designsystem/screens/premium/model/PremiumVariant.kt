package com.example.yaroslavhorach.designsystem.screens.premium.model

import com.example.yaroslavhorach.ui.UiText

sealed class PremiumVariant(open val title: UiText, open val priceFormated: UiText, open val badgeText: UiText, open val isSelected: Boolean) {
    data class Month(
        override val title: UiText,
        override val priceFormated: UiText,
        override val badgeText: UiText,
        override val isSelected: Boolean = false
    ) : PremiumVariant(title, priceFormated, badgeText, isSelected)
    data class SixMonth(
        override val title: UiText,
        override val priceFormated: UiText,
        override val badgeText: UiText,
        override val isSelected: Boolean = false
    ) : PremiumVariant(title, priceFormated, badgeText, isSelected)
    data class Forever(
        override val title: UiText,
        override val priceFormated: UiText,
        override val badgeText: UiText,
        override val isSelected: Boolean = true
    ) : PremiumVariant(title, priceFormated, badgeText, isSelected)
}