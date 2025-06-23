package com.example.yaroslavhorach.domain.prefs.model

data class UserData(
    val availableTokens: Int = 0,
    val maxTokens: Int = 0,
    val experience: Int,
    val avatarResId: Int = 0,
    val activeDays: List<Long> = emptyList(),
)