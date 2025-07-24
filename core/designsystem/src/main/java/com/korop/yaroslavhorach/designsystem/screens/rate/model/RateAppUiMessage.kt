package com.korop.yaroslavhorach.designsystem.screens.rate.model

sealed class RateAppUiMessage {
    data object RateApp : RateAppUiMessage()
    data object NavigateBack : RateAppUiMessage()
}