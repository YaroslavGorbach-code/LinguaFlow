package com.korop.yaroslavhorach.domain.prefs.model

data class UserData(
    val userName: String = "",
    val availableTokens: Int = 0,
    val maxTokens: Int = 0,
    val experience: Int = 0,
    val avatarResId: Int? = null,
    val stars: Int = 0,
    val isPremium: Boolean = false,
    val isOnboarding: Boolean = false,
    val activeDays: List<Long> = emptyList(),
    val deviceLanguage: String? = null,
    val is15MinutesTrainingAvailable: Boolean = true,
    val isMixTrainingAvailable: Boolean = true,
    )