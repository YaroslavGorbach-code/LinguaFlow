package com.example.yaroslavhorach.datastore.model
import com.example.yaroslavhorach.domain.prefs.model.UserData as DomainUserData

data class UserData(
    val availableTokens: Int = 0,
    val maxTokens: Int = 0
)

fun UserData.asDomainModel() = DomainUserData(
    availableTokens  = this.availableTokens,
    maxTokens = this.maxTokens
)