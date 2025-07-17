package com.korop.yaroslavhorach.domain.game

import com.korop.yaroslavhorach.domain.game.model.ChallengeExerciseMix
import com.korop.yaroslavhorach.domain.game.model.ChallengeTimeLimited
import com.korop.yaroslavhorach.domain.game.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(): Flow<List<Game>>
    suspend fun getGame(gameId: Long): Game?
    fun getChallengeTimeLimited(): Flow<ChallengeTimeLimited>
    fun getChallengeExerciseMix(): Flow<ChallengeExerciseMix>
    suspend fun startDailyChallenge()
    suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long)
    suspend fun requestCompleteDailyChallengeGame(name: Game.GameName)
}