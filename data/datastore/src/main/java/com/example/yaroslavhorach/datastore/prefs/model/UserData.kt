package com.example.yaroslavhorach.datastore.prefs.model
import com.example.yaroslavhorach.domain.prefs.model.UserData as DomainUserData

data class UserData(
    val availableTokens: Int = 0,
    val maxTokens: Int = 0,
    val experience: Int = 0,
    val activeDays: List<Long> = emptyList(),
    val avatarResId: Int = 0
)

fun UserData.asDomainModel() = DomainUserData(
    availableTokens  = this.availableTokens,
    maxTokens = this.maxTokens,
    experience = this.experience
)