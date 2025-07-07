package com.korop.yaroslavhorach.exercises.speaking.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.exercises.speaking.SpeakingExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class SpeakingExerciseRoute(val exerciseId: Long)

fun NavController.navigateToSpeakingExercise(exerciseId: Long) {
    navigate(route = SpeakingExerciseRoute(exerciseId)) {

    }
}

fun NavGraphBuilder.speakingExerciseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseCompleted: (time: Long, experience: Int) -> Unit
) {
    composable<SpeakingExerciseRoute> {
        SpeakingExerciseRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToExerciseResult = { time, xp ->
                onNavigateToExerciseCompleted(time, xp)
            })
    }
}