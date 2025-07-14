package com.korop.yaroslavhorach.settings.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.ui.UiText

data class SettingsViewState(
    val userName: String = "",
    val isPremiumUser: Boolean = false,
    val sections: List<SettingsSectionUi> = emptyList(),
    val uiMessage: UiMessage<SettingsUiMessage>? = null,
    val languages: List<Language> = emptyList(),
    val currentLanguage: String = "en"
) {
    companion object {
        val Empty = SettingsViewState()
        val Preview = SettingsViewState(
            userName = "Вовкобой",
            isPremiumUser = false,
            sections = listOf(
                SettingsSectionUi(
                    UiText.FromString("Title"),
                    listOf(
                        SettingsItemUi(
                            title = UiText.FromString("title"),
                            subtitle = UiText.FromString("subtitle"),
                            SettingsItemType.RATE
                        ),
                        SettingsItemUi(
                            title = UiText.FromString("title"),
                            subtitle = UiText.FromString("subtitle"),
                            SettingsItemType.FEEDBACK
                        )
                    )
                )
            )
        )
    }
}