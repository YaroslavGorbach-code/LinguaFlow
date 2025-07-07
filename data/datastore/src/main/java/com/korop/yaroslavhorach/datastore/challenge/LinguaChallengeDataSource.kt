package com.korop.yaroslavhorach.datastore.challenge

import androidx.datastore.core.DataStore
import com.korop.yaroslavhorach.datastore.ChallengeProgress
import com.korop.yaroslavhorach.datastore.challenge.model.DailyChallengeProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LinguaChallengeDataSource @Inject constructor(
    private val challengeProgress: DataStore<ChallengeProgress>
) {
    suspend fun updateChallengeProgress(progress: DailyChallengeProgress) {
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .setChallengeId(progress.id)
                .setProgressInMs(progress.progressInMs)
                .setIsStarted(progress.isStarted)
                .setAvailableDuringDate(progress.availableDuringDate)
                .build()
        }
    }

    suspend fun updateChallengeTimeProgress(time: Long){
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .setProgressInMs(prefs.progressInMs + time)
                .build()
        }
    }

    suspend fun startChallenge() {
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .setIsStarted(true)
                .build()
        }
    }

    fun getChallengeProgress(): Flow<DailyChallengeProgress> {
        return challengeProgress.data.map {
            DailyChallengeProgress(
                id = it.challengeId,
                isStarted = it.isStarted,
                availableDuringDate = it.availableDuringDate,
                progressInMs = it.progressInMs
            )
        }
    }
}