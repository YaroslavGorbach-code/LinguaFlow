package com.example.yaroslavhorach.domain.prefs

import com.example.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow

interface PrefsRepository {
    fun getUserData(): Flow<UserData>
    fun getFavoriteGamesIds(): Flow<List<Long>>
    suspend fun addGameToFavorites(gameId: Long)
    suspend fun removeGameFromFavorites(gameId: Long)
    suspend fun useToken()
    suspend fun refreshTokens()
    suspend fun markCurrentDayAsActive()
}