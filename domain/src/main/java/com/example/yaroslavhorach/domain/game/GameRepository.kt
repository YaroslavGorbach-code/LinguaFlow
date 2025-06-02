package com.example.yaroslavhorach.domain.game

import com.example.yaroslavhorach.domain.game.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
     fun getGames(): Flow<List<Game>>
     suspend fun getGame(gameId: Long): Game?
}