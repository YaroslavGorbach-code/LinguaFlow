package com.korop.yaroslavhorach.designsystem.screens.rate.model

sealed class RateAppAction {
    data object OnRateClicked : RateAppAction()
    data object OnLetterClicked : RateAppAction()
}