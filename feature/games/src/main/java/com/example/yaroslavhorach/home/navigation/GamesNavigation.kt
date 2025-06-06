package com.example.yaroslavhorach.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.yaroslavhorach.home.GamesRoute
import kotlinx.serialization.Serializable

@Serializable
data object GamesRoute

// TODO: temp remove then
@Serializable
data object ProfileRoute

fun NavController.navigateToGames(navOptions: NavOptions? = null) {
    this.navigate(GamesRoute, navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onNavigateToGame: (gameId: Long) -> Unit
) {
    composable<GamesRoute> {
        GamesRoute(onNavigateToGame)
    }
}
