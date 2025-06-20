package com.example.yaroslavhorach.data.game

import android.content.Context
import com.example.yaroslavhorach.common.utill.isToday
import com.example.yaroslavhorach.common.utill.loadJsonFromAssets
import com.example.yaroslavhorach.datastore.challenge.LinguaChallengeDataSource
import com.example.yaroslavhorach.datastore.challenge.model.DailyChallengeProgress
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.game.model.Challenge
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    private var cachedChallenge: Challenge? = null

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

    override fun getChallenge(): Flow<Challenge> {
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
                val challenges: List<Challenge> = loadJsonFromAssets(context, "daily_challenge.json")?.let { challenges ->
                    gson.fromJson(challenges, object : TypeToken<List<Challenge>>() {}.type)
                } ?: emptyList()
                emit(challenges)
            },
            prefsRepository.getUserData(),
            getGames(),
            challengeDataSource.getChallengeProgress()
        ) { challenges, userData, games, progress ->

           if (progress.availableDuringDate.isToday()) {
               val startedChallenge = challenges.find { it.id == progress.id }

               cachedChallenge = startedChallenge!!.copy(
                   status = Challenge.Status(
                       started = progress.isStarted,
                       completed = (progress.progressInMs / 60_000).toInt() >= startedChallenge.durationMinutes
                   ),
                   progressInMinutes = (progress.progressInMs / 60_000).toInt()
               )
               cachedChallenge!!
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

               challengeDataSource.updateChallengeProgress(
                   DailyChallengeProgress(
                       id = challenge.id,
                       availableDuringDate = Calendar.getInstance().timeInMillis,
                       progressInMs = 0,
                       isStarted = false,
                   )
               )
               cachedChallenge = challenge
               cachedChallenge!!
           }
       }
    }

     override suspend fun startDailyChallenge() {
        challengeDataSource.startChallenge()
    }

    override suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long) {
        if (skill.contains(cachedChallenge?.theme)) {
            withContext(Dispatchers.IO) {
                challengeDataSource.updateChallengeTimeProgress(time)
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