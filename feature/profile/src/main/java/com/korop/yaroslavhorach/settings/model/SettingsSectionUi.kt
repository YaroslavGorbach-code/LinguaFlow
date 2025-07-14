package com.korop.yaroslavhorach.settings.model

import com.korop.yaroslavhorach.ui.UiText

data class SettingsSectionUi(val title: UiText, val items: List<SettingsItemUi>)

data class SettingsItemUi(val title: UiText, val subtitle: UiText, val type: SettingsItemType)

enum class SettingsItemType {
    CHANGE_LANGUAGE, RATE, FEEDBACK
}