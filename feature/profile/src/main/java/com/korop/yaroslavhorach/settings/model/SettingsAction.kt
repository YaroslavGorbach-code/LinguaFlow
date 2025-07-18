package com.korop.yaroslavhorach.settings.model

sealed class SettingsAction {
    data object OnBackClicked : SettingsAction()
    data class OnSettingsItemClicked(val type: SettingsItemType) : SettingsAction()
    data class OnSettingsItemChecked(val type: SettingsItemType, val isChecked: Boolean) : SettingsAction()
    data class OnLanguageSelected(val language: Language) : SettingsAction()
}