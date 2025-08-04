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
import com.korop.yaroslavhorach.datastore.prefs.LinguaPrefsDataSource
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Challenge
import com.korop.yaroslavhorach.domain.game.model.ChallengeExerciseMix
import com.korop.yaroslavhorach.domain.game.model.ChallengeTimeLimited
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.domain.prefs.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefsRepository: PrefsRepository,
    private val challengeDataSource: LinguaChallengeDataSource,
    private val prefsDataSource: LinguaPrefsDataSource
) : GameRepository {
    private var cachedGames: List<Game> = emptyList()
    private var cachedChallengeTimeLimited: ChallengeTimeLimited? = null
    private var cachedChallengeExerciseMix: ChallengeExerciseMix? = null

    private var newGameUnlocked: MutableStateFlow<Game?> = MutableStateFlow(null)
    private var lastExperience = 0

    init {
        prefsRepository.getUserData()
            .map { it.experience }
            .distinctUntilChanged()
            .onEach { xp ->
                lastExperience = xp

                val newlyUnlockedGame = getGames()
                    .first()
                    .sortedBy { it.minExperienceRequired }
                    .lastOrNull { game ->
                        xp >= game.minExperienceRequired && game.minExperienceRequired > 0 &&
                                prefsRepository.getGameUnlockedScreenWasShownIds().first().any { it == game.id }.not()

                    }

                if (newlyUnlockedGame != null) {
                    newGameUnlocked.value = newlyUnlockedGame
                }
            }
            .launchIn(GlobalScope)
    }

    override fun getGames(): Flow<List<Game>> {
        return flow {
            prefsRepository.refreshTokens()

            emit(cachedGames.ifEmpty {
                cachedGames = getRawGames()
                cachedGames
            }.map { game ->
                val times = prefsDataSource.getCompletedTimesForGame(game.name)

                game.copy(completedTimes = times.first())
            })
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getGame(gameId: Long): Game {
        return getGames().first().find { it.id == gameId }!!
    }

    override fun getTodayChallenge(): Flow<Challenge> {
        return flow {
            val userData = prefsRepository.getUserData().first()
            val dayNumber = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.DAY_OF_MONTH)
            val mixProgress = challengeDataSource.getChallengeExerciseMixProgress()
            val limitedTimeProgress = challengeDataSource.getChallengeTimeLimitedProgress()


            emitAll(when {
                mixProgress.first().availableDuringDate.isToday() -> getChallengeExerciseMix()
                limitedTimeProgress.first().availableDuringDate.isToday() -> getChallengeTimeLimited()
                userData.is15MinutesTrainingAvailable && userData.isMixTrainingAvailable.not() -> getChallengeTimeLimited()
                userData.is15MinutesTrainingAvailable.not() && userData.isMixTrainingAvailable -> getChallengeExerciseMix()
                else -> if (dayNumber % 2 == 0) getChallengeExerciseMix() else getChallengeTimeLimited()
            })
        }
    }

    private fun getChallengeTimeLimited(): Flow<ChallengeTimeLimited> {
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
                val challengeTimeLimited: List<ChallengeTimeLimited> =
                    loadJsonFromAssets(context, "daily_challenge.json")?.let { challenges ->
                        gson.fromJson(challenges, object : TypeToken<List<ChallengeTimeLimited>>() {}.type)
                    } ?: emptyList()
                emit(challengeTimeLimited)
            },
            prefsRepository.getUserData(),
            getGames(),
            challengeDataSource.getChallengeTimeLimitedProgress()
        ) { challenges, userData, games, progress ->

            cachedChallengeTimeLimited = if (progress.availableDuringDate.isToday()) {
                getActiveTimeLimitedChallenge(games, userData, challenges, progress)
            } else {
                createNewTimeLimitedChallenge(games, userData, challenges)
            }
            cachedChallengeTimeLimited!!
        }
    }

    private suspend fun getActiveTimeLimitedChallenge(
        games: List<Game>,
        userData: UserData,
        challenges: List<ChallengeTimeLimited>,
        progress: DailyChallengeTimeLimitedProgress
    ): ChallengeTimeLimited {
        val startedChallenge = challenges.find { it.id == progress.id } ?: run {
            return@run createNewTimeLimitedChallenge(
                games,
                userData,
                challenges
            )
        }

        return startedChallenge.apply {
            status = Challenge.Status(
                started = progress.isStarted,
                completed = (progress.progressInMs / 60_000).toInt() >= startedChallenge.durationMinutes
            )
            progressInMinutes = (progress.progressInMs / 60_000).toInt()
        }
    }

    private suspend fun createNewTimeLimitedChallenge(
        games: List<Game>,
        userData: UserData,
        challenges: List<ChallengeTimeLimited>
    ): ChallengeTimeLimited {
        challengeDataSource.clearChallengeExerciseCompleted()
        val skillForChallenge = games
            .asSequence()
            .filter { it.minExperienceRequired <= userData.experience }
            .map { it.skills }
            .flatten()
            .toSet()
            .filter { it != Game.Skill.FLIRT }
            .random()

        val challenge = challenges.first { it.theme == skillForChallenge }

        val progress = DailyChallengeTimeLimitedProgress(
            id = challenge.id,
            availableDuringDate = Calendar.getInstance().timeInMillis,
            progressInMs = 0,
            isStarted = false,
        )
        challengeDataSource.updateChallengeTimeLimitedProgress(progress)

        return getActiveTimeLimitedChallenge(games, userData, challenges, progress)
    }

    private fun getChallengeExerciseMix(): Flow<ChallengeExerciseMix> {
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
                val challengeTimeLimited: List<ChallengeExerciseMix> =
                    loadJsonFromAssets(context, "daily_challenge_exercise_mix.json")?.let { challenges ->
                        gson.fromJson(challenges, object : TypeToken<List<ChallengeExerciseMix>>() {}.type)
                    } ?: emptyList()
                emit(challengeTimeLimited)
            },
            prefsRepository.getUserData(),
            getGames(),
            challengeDataSource.getChallengeExerciseMixProgress()
        ) { challenges, userData, games, progress ->
            cachedChallengeExerciseMix = if (progress.availableDuringDate.isToday()) {
                getActiveExerciseMixChallenge(challenges, progress, games, userData)
            } else {
                crateNewExercisesMixChallenge(games, userData, challenges)
            }
            cachedChallengeExerciseMix!!
        }
    }

    private suspend fun getActiveExerciseMixChallenge(
        challenges: List<ChallengeExerciseMix>,
        progress: DailyChallengeExerciseMixProgress,
        games: List<Game>,
        userData: UserData
    ): ChallengeExerciseMix {
        val startedChallenge = challenges.find { it.id == progress.id } ?: run {
            return@run crateNewExercisesMixChallenge(
                games,
                userData,
                challenges
            )
        }

        cachedChallengeExerciseMix = startedChallenge.apply {
            status = Challenge.Status(
                started = progress.isStarted,
                completed = progress.exercisesAndCompleted.all { it.second }
            )
            exercisesAndCompletedMark =
                progress.exercisesAndCompleted.map { Game.GameName.valueOf(it.first) to it.second }
        }
        return cachedChallengeExerciseMix!!
    }

    private suspend fun crateNewExercisesMixChallenge(
        games: List<Game>,
        userData: UserData,
        challenges: List<ChallengeExerciseMix>
    ): ChallengeExerciseMix {
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
        return cachedChallengeExerciseMix!!
    }

    override suspend fun startDailyChallenge() {
        challengeDataSource.startChallenge()
    }

    override suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long) {
        if (cachedChallengeTimeLimited?.status?.inProgress?.not() == true) return

        if (skill.contains(cachedChallengeTimeLimited?.theme)) {

            withContext(Dispatchers.IO) {
                val currentProgress = cachedChallengeTimeLimited?.progressInMinutes ?: 0
                val newProgress = currentProgress + time
                val isLastUpdate = newProgress >= (cachedChallengeTimeLimited?.durationMinutes ?: 0) * 60 * 1000

                challengeDataSource.updateChallengeTimeProgress(time)

                if (isLastUpdate) {
                    prefsRepository.addExperience(cachedChallengeTimeLimited!!.bonusOnComplete)
                }
            }
        }
    }

    override suspend fun requestCompleteDailyChallengeGame(name: Game.GameName) {
        if (cachedChallengeExerciseMix?.status?.inProgress?.not() == true) return

        if (cachedChallengeExerciseMix?.exercisesAndCompletedMark
                ?.any { it.first.name == name.name && it.second.not() } == true
        ) {
            withContext(Dispatchers.IO) {
                val isLastUpdate = challengeDataSource.getChallengeExerciseMixProgress()
                    .first().exercisesAndCompleted
                    .count { it.second.not() } == 1

                if (isLastUpdate) {
                    prefsRepository.addExperience(cachedChallengeExerciseMix!!.bonusOnComplete)
                }

                challengeDataSource.updateChallengeExerciseCompleted(name.name)
            }
        }
    }

    override fun getLastUnlockedGame(): Flow<Game?> {
        return newGameUnlocked
    }

    override suspend fun clearLastUnlockedGame() {
        newGameUnlocked.value = null
    }

    override suspend fun markGameAsCompleted(it: Game.GameName) {
        prefsRepository.markGameAsCompleted(it)
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