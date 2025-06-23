package com.example.yaroslavhorach.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.home.GamesRoute
import kotlinx.serialization.Serializable

@Serializable
data object GamesRoute

fun NavController.navigateToGames(navOptions: NavOptions? = null) {
    this.navigate(GamesRoute, navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onNavigateToGame: (gameId: Long, gameName: Game.GameName) -> Unit
) {
    composable<GamesRoute> {
        GamesRoute(onNavigateToGame)
    }
}
