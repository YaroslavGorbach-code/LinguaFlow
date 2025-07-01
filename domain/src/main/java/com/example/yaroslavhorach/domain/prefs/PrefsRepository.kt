package com.example.yaroslavhorach.domain.prefs

import com.example.yaroslavhorach.domain.prefs.model.Avatar
import com.example.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PrefsRepository {
    fun getUserData(): Flow<UserData>
    fun getFavoriteGamesIds(): Flow<List<Long>>
    suspend fun addGameToFavorites(gameId: Long)
    suspend fun removeGameFromFavorites(gameId: Long)
    suspend fun useToken()
    suspend fun refreshTokens()
    suspend fun markCurrentDayAsActive()
    fun getAvatars(): List<Avatar>
    suspend fun changeAvatar(resId: Int)
    suspend fun changeName(name: String)
    suspend fun addExperience(xp: Int)
    suspend fun activatePremium()
    suspend fun getUsedContent(name: String): Flow<List<Long>>
    suspend fun useExerciseContent(id: Long, name: String)
    suspend fun clearUsedExerciseContent(name: String)
}