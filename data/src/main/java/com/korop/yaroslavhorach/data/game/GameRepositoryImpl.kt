package com.korop.yaroslavhorach.data.game

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.korop.yaroslavhorach.common.utill.isToday
import com.korop.yaroslavhorach.common.utill.loadJsonFromAssets
import com.korop.yaroslavhorach.datastore.challenge.LinguaChallengeDataSource
import com.korop.yaroslavhorach.datastore.challenge.model.DailyChallengeExerciseMixProgress
import com.korop.yaroslavhorach.datastore.challenge.model.DailyChallengeTimeLimitedProgress
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Challenge
import com.korop.yaroslavhorach.domain.game.model.ChallengeExerciseMix
import com.korop.yaroslavhorach.domain.game.model.ChallengeTimeLimited
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefsRepository: PrefsRepository,
    private val challengeDataSource: LinguaChallengeDataSource
) : GameRepository {
    private var cachedGames: List<Game> = emptyList()
    private var cachedChallengeTimeLimited: ChallengeTimeLimited? = null
    private var cachedChallengeExerciseMix: ChallengeExerciseMix? = null

    override fun getGames(): Flow<List<Game>> {
        return flow {
            prefsRepository.refreshTokens()

            emit(cachedGames.ifEmpty {
                cachedGames = getRawGames()
                cachedGames
            })
        }
    }

    override suspend fun getGame(gameId: Long): Game {
        return cachedGames.find { it.id == gameId }!!
    }

    override fun getChallengeTimeLimited(): Flow<ChallengeTimeLimited> {
        val gson = GsonBuilder()
            .registerTypeAdapter(Game.Skill::class.java, JsonDeserializer { json, _, _ ->
                try {
                    Game.Skill.valueOf(json.asString)
                } catch (e: Exception) {
                    null
                }
            })
            .create()

       return kotlinx.coroutines.flow.combine(
            flow {
                val challengeTimeLimited: List<ChallengeTimeLimited> = loadJsonFromAssets(context, "daily_challenge.json")?.let { challenges ->
                    gson.fromJson(challenges, object : TypeToken<List<ChallengeTimeLimited>>() {}.type)
                } ?: emptyList()
                emit(challengeTimeLimited)
            },
            prefsRepository.getUserData(),
            getGames(),
            challengeDataSource.getChallengeTimeLimitedProgress()
        ) { challenges, userData, games, progress ->

           if (progress.availableDuringDate.isToday()) {
               val startedChallenge = challenges.find { it.id == progress.id }

               cachedChallengeTimeLimited = startedChallenge?.apply {
                   status = Challenge.Status(
                       started = progress.isStarted,
                       completed = (progress.progressInMs / 60_000).toInt() >= startedChallenge.durationMinutes
                   )
                   progressInMinutes = (progress.progressInMs / 60_000).toInt()
               }
               cachedChallengeTimeLimited!!
           } else{
               val skillForChallenge = games
                   .asSequence()
                   .filter { it.minExperienceRequired <= userData.experience }
                   .map { it.skills }
                   .flatten()
                   .toSet()
                   .filter { it != Game.Skill.FLIRT }
                   .random()

               val challenge = challenges.first { it.theme == skillForChallenge }

               challengeDataSource.updateChallengeTimeLimitedProgress(
                   DailyChallengeTimeLimitedProgress(
                       id = challenge.id,
                       availableDuringDate = Calendar.getInstance().timeInMillis,
                       progressInMs = 0,
                       isStarted = false,
                   )
               )
               cachedChallengeTimeLimited = challenge
               cachedChallengeTimeLimited!!
           }
       }
    }

    override fun getChallengeExerciseMix(): Flow<ChallengeExerciseMix> {
        val gson = GsonBuilder()
            .registerTypeAdapter(Game.Skill::class.java, JsonDeserializer { json, _, _ ->
                try {
                    Game.Skill.valueOf(json.asString)
                } catch (e: Exception) {
                    null
                }
            })
            .create()

        return kotlinx.coroutines.flow.combine(
            flow {
                val challengeTimeLimited: List<ChallengeExerciseMix> = loadJsonFromAssets(context, "daily_challenge_exercise_mix.json")?.let { challenges ->
                    gson.fromJson(challenges, object : TypeToken<List<ChallengeExerciseMix>>() {}.type)
                } ?: emptyList()
                emit(challengeTimeLimited)
            },
            prefsRepository.getUserData(),
            getGames(),
            challengeDataSource.getChallengeExerciseMixProgress()
        ) { challenges, userData, games, progress ->

            if (progress.availableDuringDate.isToday()) {
                val startedChallenge = challenges.find { it.id == progress.id }

                cachedChallengeExerciseMix = startedChallenge?.apply {
                    status = Challenge.Status(
                        started = progress.isStarted,
                        completed = progress.exercisesAndCompleted.all { it.second }
                    )
                    exercisesAndCompletedMark = progress.exercisesAndCompleted.map { Game.GameName.valueOf(it.first) to it.second }
                }
                cachedChallengeExerciseMix!!
            } else {
                val filteredGames = games
                    .filter { it.minExperienceRequired <= userData.experience }

                val skills = filteredGames
                    .flatMap { it.skills }
                    .toSet()
                    .filter { it != Game.Skill.FLIRT }

                val usedGames = mutableSetOf<Game>()
                val gamesForChallenge = mutableListOf<Game>()

                skills.forEach { skill ->
                    val available = filteredGames
                        .filter { it.skills.contains(skill) && it !in usedGames }

                    val selected = available.randomOrNull()

                    if (selected != null) {
                        usedGames.add(selected)
                        gamesForChallenge.add(selected)
                    }
                }

                val challenge = challenges.first().apply {
                    exercisesAndCompletedMark = gamesForChallenge.map { it.name to false }
                }

                challengeDataSource.clearChallengeExerciseCompleted()
                challengeDataSource.updateChallengeExerciseMixProgress(
                    DailyChallengeExerciseMixProgress(
                        id = challenge.id,
                        availableDuringDate = Calendar.getInstance().timeInMillis,
                        exercisesAndCompleted = gamesForChallenge.map { it.name.name to false },
                        isStarted = false,
                    )
                )
                cachedChallengeExerciseMix = challenge
                cachedChallengeExerciseMix!!
            }
        }
    }

    override suspend fun startDailyChallenge() {
        challengeDataSource.startChallenge()
    }

    override suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long) {
        if (skill.contains(cachedChallengeTimeLimited?.theme)) {

            withContext(Dispatchers.IO) {
                val currentProgress = cachedChallengeTimeLimited?.progressInMinutes ?: 0
                val newProgress = currentProgress + time
                val isLatUpdate = newProgress >= (cachedChallengeTimeLimited?.durationMinutes ?: 0) * 60 * 1000

                challengeDataSource.updateChallengeTimeProgress(time)

                if (isLatUpdate) {
                    prefsRepository.addExperience(cachedChallengeExerciseMix!!.bonusOnComplete)
                }
            }
        }
    }

    override suspend fun requestCompleteDailyChallengeGame(name: Game.GameName) {
        if (cachedChallengeExerciseMix?.exercisesAndCompletedMark
                ?.any { it.first.name == name.name && it.second.not() } == true
        ) {
            withContext(Dispatchers.IO) {
                val isLastUpdate = challengeDataSource.getChallengeExerciseMixProgress()
                    .first().exercisesAndCompleted
                    .map { it.second }
                    .toSet()
                    .size == 1

                if (isLastUpdate){
                    prefsRepository.addExperience(cachedChallengeExerciseMix!!.bonusOnComplete)
                }

                challengeDataSource.updateChallengeExerciseCompleted(name.name)
            }
        }
    }

    private fun getRawGames(): List<Game> {
        val gson = GsonBuilder()
            .registerTypeAdapter(Game.Skill::class.java, JsonDeserializer { json, _, _ ->
                try {
                    Game.Skill.valueOf(json.asString)
                } catch (e: Exception) {
                    null
                }
            })
            .create()

        return loadJsonFromAssets(context, "games.json")?.let { games ->
            gson.fromJson(games, object : TypeToken<List<Game>>() {}.type)
        } ?: emptyList()
    }
}