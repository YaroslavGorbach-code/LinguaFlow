package com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.GameUnlockedSuccessRoute
import kotlinx.serialization.Serializable

@Serializable
data class GameUnlockedSuccessNavigation(val gameId: Long)

fun NavController.navigateToGameUnlockedSuccess(gameId: Long) {
    navigate(route = GameUnlockedSuccessNavigation(gameId)) {
        navOptions {
            popBackStack()
        }
    }
}

fun NavGraphBuilder.gameUnlockedScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGame: (id: Long) -> Unit,
) {
    composable<GameUnlockedSuccessNavigation> {
        GameUnlockedSuccessRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToGame = onNavigateToGame
        )
    }
}