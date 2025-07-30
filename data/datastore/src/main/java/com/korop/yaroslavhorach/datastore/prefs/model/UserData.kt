package com.korop.yaroslavhorach.datastore.prefs.model

import com.korop.yaroslavhorach.domain.prefs.model.UserData as DomainUserData

data class UserData(
    val userName: String,
    val availableTokens: Int = 0,
    val maxTokens: Int = 0,
    val experience: Int = 0,
    val stars: Int = 0,
    val activeDays: List<Long> = emptyList(),
    val avatarResId: Int? = null,
    val isPremium: Boolean = false,
    val isOnboarding: Boolean = false,
    val deviceLanguage: String? = null,
    val is15MinutesTrainingAvailable: Boolean = true,
    val isMixTrainingAvailable: Boolean = true,
)

fun UserData.asDomainModel() = DomainUserData(
    userName = this.userName,
    availableTokens = this.availableTokens,
    maxTokens = this.maxTokens,
    experience = this.experience,
    stars = this.stars,
    activeDays = this.activeDays,
    avatarResId = if (this.avatarResId == 0) null else this.avatarResId,
    isPremium = this.isPremium,
    isOnboarding = this.isOnboarding,
    deviceLanguage = this.deviceLanguage?.takeIf { it.isNotEmpty() },
    isMixTrainingAvailable = this.isMixTrainingAvailable,
    is15MinutesTrainingAvailable = this.is15MinutesTrainingAvailable,
)