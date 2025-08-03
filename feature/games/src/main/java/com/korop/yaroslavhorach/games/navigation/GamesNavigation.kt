package com.korop.yaroslavhorach.games.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.games.GamesRoute
import kotlinx.serialization.Serializable

@Serializable
data class GamesRoute(val gameId: Long? = null)

fun NavController.navigateToGames(navOptions: NavOptions? = null, gameId: Long? = null) {
    this.navigate(GamesRoute(gameId), navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onNavigateToGame: (gameId: Long, gameName: Game.GameName) -> Unit,
    onNavigateToPremium: () -> Unit,
) {
    composable<GamesRoute> {
        GamesRoute(onNavigateToGame, onNavigateToPremium)
    }
}