package com.example.yaroslavhorach.datastore.prefs

import androidx.datastore.core.DataStore
import com.example.yaroslavhorach.common.utill.isToday
import com.example.yaroslavhorach.datastore.R
import com.example.yaroslavhorach.datastore.UserPreferences
import com.example.yaroslavhorach.datastore.prefs.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class LinguaPrefsDataSource @Inject constructor(private val userPreferences: DataStore<UserPreferences>) {
    val userData: Flow<UserData> = userPreferences.data
        .map {
            UserData(
                availableTokens = it.availableTokens,
                maxTokens = 3,
                experience = it.experience,
                avatarResId = it.avatarRes,
                activeDays = it.activeDaysList,
                userName = it.name ?: "",
                isPremium = it.isPremium
            )
        }

    suspend fun refreshTokens() {
        userPreferences.updateData { prefs ->
            if (prefs.lastTimeTokenWasUsed.isToday().not()) {
                prefs.toBuilder()
                    .setAvailableTokens(3)
                    .build()
            } else {
                prefs
            }
        }
    }

    suspend fun addCurrentDayToActiveDays() {
        userPreferences.updateData { prefs ->
            if (prefs.activeDaysList.any { it.isToday() }.not()) {
                prefs.toBuilder()
                    .addActiveDays(Date().time)
                    .build()
            } else {
                prefs
            }
        }
    }

    fun getFavoriteGamesIds(): Flow<List<Long>> {
        return userPreferences.data.map { it.favoriteGamesList }
    }

    suspend fun addGameToFavorites(gameId: Long) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .addFavoriteGames(gameId)
                .build()
        }
    }

    suspend fun changeAvatar(avatarResId: Int) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setAvatarRes(avatarResId)
                .build()
        }
    }

    suspend fun changeName(name: String) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setName(name)
                .build()
        }
    }

    suspend fun changePremiumState(isPremium: Boolean) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setIsPremium(isPremium)
                .build()
        }
    }

    suspend fun removeGameFromFavorites(gameId: Long) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .clearFavoriteGames()
                .addAllFavoriteGames(
                    prefs.favoriteGamesList.filter { it != gameId }
                )
                .build()
        }
    }

    suspend fun useToken() {
        userPreferences.updateData { prefs ->
            if (prefs.availableTokens > 0) {
                prefs.toBuilder()
                    .setLastTimeTokenWasUsed(Date().time)
                    .setAvailableTokens(prefs.availableTokens.dec())
                    .build()
            } else {
                prefs
            }
        }
    }
}