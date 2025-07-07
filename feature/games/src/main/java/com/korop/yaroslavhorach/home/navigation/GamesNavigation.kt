package com.korop.yaroslavhorach.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.home.GamesRoute
import kotlinx.serialization.Serializable

@Serializable
data object GamesRoute

fun NavController.navigateToGames(navOptions: NavOptions? = null) {
    this.navigate(GamesRoute, navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onNavigateToGame: (gameId: Long, gameName: Game.GameName) -> Unit,
    onNavigateToPremium: () -> Unit,
) {
    composable<GamesRoute> {
        GamesRoute(onNavigateToGame, onNavigateToPremium)
    }
}
