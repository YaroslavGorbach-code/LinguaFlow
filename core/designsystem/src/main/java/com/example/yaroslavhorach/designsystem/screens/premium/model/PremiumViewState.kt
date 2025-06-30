package com.example.yaroslavhorach.designsystem.screens.premium.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.ui.UiText

data class PremiumViewState(
    val variants: List<PremiumVariant>,
    val uiMessage: UiMessage<PremiumUiMessage>? = null
) {

    companion object {
        val Empty = PremiumViewState(emptyList())
        val Preview = PremiumViewState(
            listOf(
                PremiumVariant.Month("Місячна", UiText.FromString("99 ₴/місяць"), UiText.Empty),
                PremiumVariant.SixMonth(
                    "6 - місячна",
                    UiText.FromString("239 ₴/півроку"),
                    UiText.FromString("39 ₴/місяць")
                ),
                PremiumVariant.Forever("Постійна", UiText.FromString("399 ₴/∞"), UiText.FromString("Найвигідніше"))
            ),
        )
    }
}
