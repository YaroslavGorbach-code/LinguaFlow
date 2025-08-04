package com.korop.yaroslavhorach.exercises.exercise_completed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.exercises.exercise_completed.ExerciseCompletedRoute
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseCompletedRoute(val experience: Int, val time: Long, val completedGameName: Game.GameName?)

fun NavController.navigateToExerciseCompleted(experience: Int, time: Long, completedGameName: Game.GameName?) {
    navigate(route = ExerciseCompletedRoute(experience, time, completedGameName)) {
        navOptions {
            popBackStack()
        }
    }
}

fun NavGraphBuilder.exerciseCompletedScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGameUnlocked: (gameId: Long) -> Unit,
    onNavigateToRateApp: () -> Unit
) {
    composable<ExerciseCompletedRoute> {
        ExerciseCompletedRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToGameUnlocked = onNavigateToGameUnlocked,
            onNavigateToRateApp = onNavigateToRateApp
        )
    }
}