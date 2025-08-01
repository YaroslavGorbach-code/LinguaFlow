package com.korop.yaroslavhorach.data.prefs.repository

import com.korop.yaroslavhorach.datastore.prefs.LinguaPrefsDataSource
import com.korop.yaroslavhorach.datastore.prefs.model.asDomainModel
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.domain.prefs.model.Avatar
import com.korop.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
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

    override suspend fun deactivatePremium() {
        prefsDataSource.changePremiumState(false)
    }

    override suspend fun getUsedContent(name: String): Flow<List<Long>> {
        return prefsDataSource.getUsedContent(name)
    }

    override fun getGameUnlockedScreenWasShownIds(): Flow<List<Long>> {
       return prefsDataSource.getGameUnlockedScreenWasShownIds()
    }

    override suspend fun markScreenGameUnlockedWasShown(gameId: Long) {
        prefsDataSource.markGameUnlockedScreenWasShown(gameId)
    }

    override suspend fun useExerciseContent(id: Long, name: String) {
        prefsDataSource.useExerciseContent(id, name)
    }

    override suspend fun clearUsedExerciseContent(name: String) {
        prefsDataSource.clearUsedExerciseContent(name)
    }

    override suspend fun finishOnboarding() {
        prefsDataSource.finishOnboarding()
    }

    override suspend fun markAppAsRated() {
        prefsDataSource.markAppAsRated()
    }

    override suspend fun markRateDelayed() {
        prefsDataSource.setLastTimeAskedUserToRateApp()
    }

    override fun getIsRateAppAllowed(): Flow<Boolean> {
        return flow {
            val isAlreadyRated = prefsDataSource.getIsAppRated().first()
            val lastTimeAskedMillis = prefsDataSource.getLastTimeAskedUserToRateApp().first()
            val now = System.currentTimeMillis()
            val userXp = prefsDataSource.userData.first().experience

            val daysPassed = TimeUnit.MILLISECONDS.toDays(now - lastTimeAskedMillis)
            emit(isAlreadyRated.not() && daysPassed >= 4 && userXp >= 100)
        }
    }

    override fun getSupportedAppLanguages(): List<String> {
        return listOf("en", "uk", "ru")
    }

    override suspend fun changeLanguage(lang: String) {
        prefsDataSource.changeLanguage(lang)
    }

    override suspend fun change15MinutesTopicDailyTrainingActive(isActive: Boolean) {
        prefsDataSource.change15MinutesTrainingAvailable(isActive)
    }

    override suspend fun changeMixDailyTrainingActive(isActive: Boolean) {
        prefsDataSource.changeMixTrainingAvailable(isActive)
    }

    override fun getAvatars(): List<Avatar> {
        val premiumAvatars = listOf(
            R.drawable.im_avatar_5,
            R.drawable.im_avatar_6,
            R.drawable.im_avatar_7,
            R.drawable.im_avatar_8,
            R.drawable.im_avatar_9,
            R.drawable.im_avatar_10,
            R.drawable.im_avatar_11,
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
            R.drawable.im_avatar_27,
            R.drawable.im_avatar_28,
            R.drawable.im_avatar_29,
            R.drawable.im_avatar_30,
            R.drawable.im_avatar_31,
            R.drawable.im_avatar_32,
            R.drawable.im_avatar_33,
            R.drawable.im_avatar_34,
            R.drawable.im_avatar_35,
            R.drawable.im_avatar_36,
            R.drawable.im_avatar_37,
            R.drawable.im_avatar_38
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