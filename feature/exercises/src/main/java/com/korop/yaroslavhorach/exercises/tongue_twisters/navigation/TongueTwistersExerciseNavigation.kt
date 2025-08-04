package com.korop.yaroslavhorach.exercises.tongue_twisters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.exercises.tongue_twisters.TongueTwisterExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class TongueTwistersExerciseRoute(val exerciseId: Long)

fun NavController.navigateToTongueTwistersExercise(exerciseId: Long, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = TongueTwistersExerciseRoute(exerciseId), builder)
}

fun NavGraphBuilder.tongueTwistersExerciseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseCompleted: (time: Long, experience: Int, gameName: Game.GameName) -> Unit
) {
    composable<TongueTwistersExerciseRoute> {
        TongueTwisterExerciseRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToExerciseResult = { time, xp, gameName ->
                onNavigateToExerciseCompleted(time, xp, gameName)
            })
    }
}