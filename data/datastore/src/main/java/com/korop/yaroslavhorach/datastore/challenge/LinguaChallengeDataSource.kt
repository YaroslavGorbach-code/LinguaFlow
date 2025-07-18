package com.korop.yaroslavhorach.datastore.challenge

import androidx.datastore.core.DataStore
import com.korop.yaroslavhorach.datastore.ChallengeProgress
import com.korop.yaroslavhorach.datastore.ExerciseProgress
import com.korop.yaroslavhorach.datastore.challenge.model.DailyChallengeExerciseMixProgress
import com.korop.yaroslavhorach.datastore.challenge.model.DailyChallengeTimeLimitedProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LinguaChallengeDataSource @Inject constructor(
    private val challengeProgress: DataStore<ChallengeProgress>
) {
    suspend fun updateChallengeTimeLimitedProgress(progress: DailyChallengeTimeLimitedProgress) {
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .setChallenge15MinutesId(progress.id)
                .setProgressInMs(progress.progressInMs)
                .setIsStarted(progress.isStarted)
                .setTimedChallengeAvailableDuringDate(progress.availableDuringDate)
                .build()
        }
    }

    suspend fun updateChallengeExerciseMixProgress(progress: DailyChallengeExerciseMixProgress) {
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .setChallengeMixId(progress.id)
                .setIsStarted(progress.isStarted)
                .setMixChallengeAvailableDuringDate(progress.availableDuringDate)
                .addAllExercisesAndCompleted(progress.exercisesAndCompleted.map {
                    ExerciseProgress
                        .newBuilder()
                        .setName(it.first)
                        .setCompleted(it.second)
                        .build()
                })
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

    suspend fun updateChallengeExerciseCompleted(exerciseName: String) {
        challengeProgress.updateData { prefs ->

            val updatedExercises = prefs.exercisesAndCompletedList.map { exercise ->
                if (exercise.name == exerciseName) {
                    exercise.toBuilder()
                        .setCompleted(true)
                        .build()
                } else {
                    exercise
                }
            }

            prefs.toBuilder()
                .clearExercisesAndCompleted()
                .addAllExercisesAndCompleted(updatedExercises)
                .build()
        }
    }

    suspend fun clearChallengeExerciseCompleted() {
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .clearExercisesAndCompleted()
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

    fun getChallengeTimeLimitedProgress(): Flow<DailyChallengeTimeLimitedProgress> {
        return challengeProgress.data.map {
            DailyChallengeTimeLimitedProgress(
                id = it.challenge15MinutesId,
                isStarted = it.isStarted,
                availableDuringDate = it.timedChallengeAvailableDuringDate,
                progressInMs = it.progressInMs
            )
        }
    }

    fun getChallengeExerciseMixProgress(): Flow<DailyChallengeExerciseMixProgress> {
        return challengeProgress.data.map {
            DailyChallengeExerciseMixProgress(
                id = it.challengeMixId,
                isStarted = it.isStarted,
                availableDuringDate = it.mixChallengeAvailableDuringDate,
                exercisesAndCompleted = it.exercisesAndCompletedList.map { it.name to it.completed }
            )
        }
    }
}