package com.example.yaroslavhorach.domain.prefs

import com.example.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow

interface PrefsRepository {
    fun getUserData(): Flow<UserData>
    suspend fun useToken()
    suspend fun refreshTokens()
}