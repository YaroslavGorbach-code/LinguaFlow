package com.example.yaroslavhorach.domain.game

import com.example.yaroslavhorach.domain.game.model.Challenge
import com.example.yaroslavhorach.domain.game.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(): Flow<List<Game>>
    suspend fun getGame(gameId: Long): Game?
    fun getChallenge(): Flow<Challenge>
    suspend fun startDailyChallenge()
    suspend fun requestUpdateDailyChallengeCompleteTime(skill: List<Game.Skill>, time: Long)
}