package com.korop.yaroslavhorach.domain.game

import com.korop.yaroslavhorach.domain.game.model.Challenge
import com.korop.yaroslavhorach.domain.game.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(): Flow<List<Game>>
    suspend fun getGame(gameId: Long): Game?
    fun getTodayChallenge(): Flow<Challenge>
    suspend fun startDailyChallenge()
    suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long)
    suspend fun requestCompleteDailyChallengeGame(name: Game.GameName)
}