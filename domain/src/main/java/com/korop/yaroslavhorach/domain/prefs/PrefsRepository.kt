package com.korop.yaroslavhorach.domain.prefs

import com.korop.yaroslavhorach.domain.prefs.model.Avatar
import com.korop.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow

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
    suspend fun deactivatePremium()
    suspend fun getUsedContent(name: String): Flow<List<Long>>
    fun getGameUnlockedScreenWasShownIds(): Flow<List<Long>>
    suspend fun markScreenGameUnlockedWasShown(gameId: Long)
    suspend fun useExerciseContent(id: Long, name: String)
    suspend fun clearUsedExerciseContent(name: String)
    suspend fun finishOnboarding()
    suspend fun markAppAsRated()
    suspend fun markRateDelayed()
    fun getSupportedAppLanguages(): List<String>
    fun getIsRateAppAllowed(): Flow<Boolean>
    suspend fun changeLanguage(lang: String)
    suspend fun change15MinutesTopicDailyTrainingActive(isActive: Boolean)
    suspend fun changeMixDailyTrainingActive(isActive: Boolean)
}