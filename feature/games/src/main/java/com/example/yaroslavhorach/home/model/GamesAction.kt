package com.example.yaroslavhorach.home.model

sealed class GamesAction {
    data object OnStartDailyChallengeClicked : GamesAction()
}