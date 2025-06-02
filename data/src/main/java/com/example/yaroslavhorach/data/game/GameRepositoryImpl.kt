package com.example.yaroslavhorach.data.game

import android.content.Context
import com.example.yaroslavhorach.common.utill.loadJsonFromAssets
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefsRepository: PrefsRepository
) : GameRepository {
    private var cachedGames: List<Game> = emptyList()

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