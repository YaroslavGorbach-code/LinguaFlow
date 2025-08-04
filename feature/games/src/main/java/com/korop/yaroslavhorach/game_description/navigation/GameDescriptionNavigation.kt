package com.korop.yaroslavhorach.game_description.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.game_description.GameDescriptionRoute
import kotlinx.serialization.Serializable

@Serializable
data class GameDescriptionNavigation(val gameId: Long, val useToken: Boolean)

fun NavController.navigateToGameDescription(gameId: Long, useToken: Boolean, builder: NavOptionsBuilder.() -> Unit) {
    navigate(route = GameDescriptionNavigation(gameId, useToken), builder)
}

fun NavGraphBuilder.gameDescriptionScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGame: (id: Long, name: Game.GameName) -> Unit,
    onNavigateToPremium: () -> Unit,
) {
    composable<GameDescriptionNavigation> {
        GameDescriptionRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToGame = onNavigateToGame,
            onNavigateToPremium = onNavigateToPremium
        )
    }
}