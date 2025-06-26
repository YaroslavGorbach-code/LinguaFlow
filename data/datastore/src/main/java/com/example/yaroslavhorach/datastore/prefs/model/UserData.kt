package com.example.yaroslavhorach.datastore.prefs.model

import com.example.yaroslavhorach.domain.prefs.model.UserData as DomainUserData

data class UserData(
    val userName: String,
    val availableTokens: Int = 0,
    val maxTokens: Int = 0,
    val experience: Int = 0,
    val activeDays: List<Long> = emptyList(),
    val avatarResId: Int? = null,
    val isPremium: Boolean = false,
)

fun UserData.asDomainModel() = DomainUserData(
    userName = this.userName,
    availableTokens = this.availableTokens,
    maxTokens = this.maxTokens,
    experience = this.experience,
    activeDays = this.activeDays,
    avatarResId = if (this.avatarResId == 0) null else this.avatarResId,
    isPremium = this.isPremium
)