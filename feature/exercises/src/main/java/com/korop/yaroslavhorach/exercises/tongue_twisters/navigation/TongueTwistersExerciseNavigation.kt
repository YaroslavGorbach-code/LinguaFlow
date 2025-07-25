package com.korop.yaroslavhorach.exercises.tongue_twisters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.exercises.tongue_twisters.TongueTwisterExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class TongueTwistersExerciseRoute(val exerciseId: Long)

fun NavController.navigateToTongueTwistersExercise(exerciseId: Long) {
    navigate(route = TongueTwistersExerciseRoute(exerciseId)) {
        navOptions {

        }
    }
}

fun NavGraphBuilder.tongueTwistersExerciseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseCompleted: (time: Long, experience: Int) -> Unit
) {
    composable<TongueTwistersExerciseRoute> {
        TongueTwisterExerciseRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToExerciseResult = { time, xp ->
                onNavigateToExerciseCompleted(time, xp)
            })
    }
}