package com.example.yaroslavhorach.domain.prefs.model

data class UserData(
    val userName: String,
    val availableTokens: Int = 0,
    val maxTokens: Int = 0,
    val experience: Int,
    val avatarResId: Int? = null,
    val isPremium: Boolean = false,
    val activeDays: List<Long> = emptyList(),
)