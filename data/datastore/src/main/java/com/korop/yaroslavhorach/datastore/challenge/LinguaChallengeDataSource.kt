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
                .setChallengeId(progress.id)
                .setProgressInMs(progress.progressInMs)
                .setIsStarted(progress.isStarted)
                .setAvailableDuringDate(progress.availableDuringDate)
                .build()
        }
    }

    suspend fun updateChallengeExerciseMixProgress(progress: DailyChallengeExerciseMixProgress) {
        challengeProgress.updateData { prefs ->
            prefs.toBuilder()
                .setChallengeId(progress.id)
                .setIsStarted(progress.isStarted)
                .setAvailableDuringDate(progress.availableDuringDate)
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
                id = it.challengeId,
                isStarted = it.isStarted,
                availableDuringDate = it.availableDuringDate,
                progressInMs = it.progressInMs
            )
        }
    }

    fun getChallengeExerciseMixProgress(): Flow<DailyChallengeExerciseMixProgress> {
        return challengeProgress.data.map {
            DailyChallengeExerciseMixProgress(
                id = it.challengeId,
                isStarted = it.isStarted,
                availableDuringDate = it.availableDuringDate,
                exercisesAndCompleted = it.exercisesAndCompletedList.map { it.name to it.completed }
            )
        }
    }
}