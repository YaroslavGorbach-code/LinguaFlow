package com.korop.yaroslavhorach.games.words_game.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.games.words_game.WordsGameRoute
import kotlinx.serialization.Serializable

@Serializable
data class WordsGameNavigationRoute(val gameId: Long)

fun NavController.navigateToWordsGame (gameId: Long) {
    navigate(route = WordsGameNavigationRoute(gameId)) {
        navOptions {
            popBackStack()
        }
    }
}

fun NavGraphBuilder.wordsGameScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseCompleted: (time: Long, experience: Int, gameName: Game.GameName) -> Unit
) {
    composable<WordsGameNavigationRoute> {
        WordsGameRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToExerciseResult = { time, xp, name ->
                onNavigateToExerciseCompleted(time, xp, name)
            })
    }
}