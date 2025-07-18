package com.korop.yaroslavhorach.datastore.prefs

import androidx.datastore.core.DataStore
import com.korop.yaroslavhorach.common.utill.isToday
import com.korop.yaroslavhorach.datastore.IntList
import com.korop.yaroslavhorach.datastore.UserPreferences
import com.korop.yaroslavhorach.datastore.prefs.model.UserData
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
                isPremium = it.isPremium,
                isOnboarding = !it.isOnboarding,
                deviceLanguage = it.deviceLanguage,
                isMixTrainingAvailable = !it.isMixTaringActive,
                is15MinutesTrainingAvailable = !it.is15MinuteTaringActive
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

    fun getUsedContent(name: String): Flow<List<Long>> {
        return userPreferences.data.map { it.usedExercisesContentMap[name]?.valuesList ?: emptyList() }
    }

    suspend fun useExerciseContent(id: Long, name: String) {
        userPreferences.updateData { prefs ->
            val currentList = prefs.usedExercisesContentMap[name]?.valuesList?.toMutableList() ?: mutableListOf()

            currentList.add(id)

            val updatedIntList = IntList.newBuilder()
                .addAllValues(currentList)
                .build()

            prefs.toBuilder()
                .putUsedExercisesContent(name, updatedIntList)
                .build()
        }
    }

    suspend fun clearUsedExerciseContent(name: String) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder().putUsedExercisesContent(name, IntList.getDefaultInstance()).build()
        }
    }

    suspend fun changeAvatar(avatarResId: Int) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setAvatarRes(avatarResId)
                .build()
        }
    }

    suspend fun finishOnboarding() {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setIsOnboarding(true)
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

    suspend fun addExperience(xp: Int) {
        userPreferences.updateData { prefs ->
            val newXp = prefs.experience + xp

            prefs.toBuilder()
                .setExperience(newXp)
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

    suspend fun changeLanguage(lang: String) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setDeviceLanguage(lang)
                .build()
        }
    }
    suspend fun changeMixTrainingAvailable(isAvailable: Boolean) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setIsMixTaringActive(!isAvailable)
                .build()
        }
    }

    suspend fun change15MinutesTrainingAvailable(isAvailable: Boolean) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setIs15MinuteTaringActive(!isAvailable)
                .build()
        }
    }
}