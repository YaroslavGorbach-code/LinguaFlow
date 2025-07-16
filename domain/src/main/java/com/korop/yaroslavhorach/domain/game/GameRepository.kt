package com.korop.yaroslavhorach.domain.game

import com.korop.yaroslavhorach.domain.game.model.ChallengeTimeLimited
import com.korop.yaroslavhorach.domain.game.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(): Flow<List<Game>>
    suspend fun getGame(gameId: Long): Game?
    fun getChallengeTimeLimited(): Flow<ChallengeTimeLimited>
    suspend fun startDailyChallenge()
    suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long)
}