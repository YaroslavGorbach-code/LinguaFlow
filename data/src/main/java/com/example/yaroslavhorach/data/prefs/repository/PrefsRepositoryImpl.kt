package com.example.yaroslavhorach.data.prefs.repository

import com.example.yaroslavhorach.datastore.prefs.LinguaPrefsDataSource
import com.example.yaroslavhorach.datastore.prefs.model.asDomainModel
import com.example.yaroslavhorach.designsystem.R
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.domain.prefs.model.Avatar
import com.example.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrefsRepositoryImpl @Inject constructor(
    private val prefsDataSource: LinguaPrefsDataSource
) : PrefsRepository {
    override fun getUserData(): Flow<UserData> {
        return prefsDataSource.userData.map { it.asDomainModel() }
    }

    override fun getFavoriteGamesIds(): Flow<List<Long>> {
        return prefsDataSource.getFavoriteGamesIds()
    }

    override suspend fun addGameToFavorites(gameId: Long) {
        prefsDataSource.addGameToFavorites(gameId)
    }

    override suspend fun removeGameFromFavorites(gameId: Long) {
        prefsDataSource.removeGameFromFavorites(gameId)
    }

    override suspend fun useToken() {
        prefsDataSource.useToken()
    }

    override suspend fun refreshTokens() {
        prefsDataSource.refreshTokens()
    }

    override suspend fun markCurrentDayAsActive() {
        prefsDataSource.addCurrentDayToActiveDays()
    }

    override suspend fun changeAvatar(resId: Int) {
        prefsDataSource.changeAvatar(resId)
    }

    override suspend fun changeName(name: String) {
        prefsDataSource.changeName(name)
    }

    override suspend fun addExperience(xp: Int) {
        prefsDataSource.addExperience(xp)
    }

    override suspend fun activatePremium() {
        prefsDataSource.changePremiumState(true)
    }

    override suspend fun getUsedContent(name: String): Flow<List<Long>> {
        return prefsDataSource.getUsedContent(name)
    }

    override suspend fun useExerciseContent(id: Long, name: String) {
        prefsDataSource.useExerciseContent(id, name)
    }

    override suspend fun clearUsedExerciseContent(name: String) {
        prefsDataSource.clearUsedExerciseContent(name)
    }

    override fun getAvatars(): List<Avatar> {
        val premiumAvatars = listOf(
            R.drawable.im_avatar_5,
            R.drawable.im_avatar_6,
            R.drawable.im_avatar_7,
            R.drawable.im_avatar_8,
            R.drawable.im_avatar_9,
            R.drawable.im_avatar_10,
            R.drawable.im_avatar_12,
            R.drawable.im_avatar_13,
            R.drawable.im_avatar_14,
            R.drawable.im_avatar_15,
            R.drawable.im_avatar_16,
            R.drawable.im_avatar_17,
            R.drawable.im_avatar_18,
            R.drawable.im_avatar_19,
            R.drawable.im_avatar_20,
            R.drawable.im_avatar_21,
            R.drawable.im_avatar_22,
            R.drawable.im_avatar_23,
            R.drawable.im_avatar_24,
            R.drawable.im_avatar_25,
            R.drawable.im_avatar_26,
            R.drawable.im_avatar_28,
            R.drawable.im_avatar_29,
            R.drawable.im_avatar_30,
            R.drawable.im_avatar_31
        )

        return buildList {
            add(
                Avatar(
                    resId = R.drawable.im_avatar_1,
                    userChosen = true,
                    isPremium = false
                )
            )
            add(
                Avatar(
                    resId = R.drawable.im_avatar_2,
                    userChosen = false,
                    isPremium = false
                )
            )
            add(
                Avatar(
                    resId = R.drawable.im_avatar_3,
                    userChosen = false,
                    isPremium = false
                )
            )
            add(
                Avatar(
                    resId = R.drawable.im_avatar_4,
                    userChosen = false,
                    isPremium = false
                )
            )
            premiumAvatars.forEach { resId ->
                add(
                    Avatar(
                        resId = resId,
                        userChosen = false,
                        isPremium = true
                    )
                )
            }
        }
    }
}