package com.korop.yaroslavhorach.settings.model

sealed class SettingsUiMessage {
     data object ToRateApp: SettingsUiMessage()
     data object ToGiveFeedback: SettingsUiMessage()
     data object ShowChooseLanguageBottomSheet: SettingsUiMessage()
     data object ShowDailyTrainingChangesToast: SettingsUiMessage()
}