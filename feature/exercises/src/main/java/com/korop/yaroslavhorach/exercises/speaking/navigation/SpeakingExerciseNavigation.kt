package com.korop.yaroslavhorach.exercises.speaking.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.exercises.speaking.SpeakingExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class SpeakingExerciseRoute(val exerciseId: Long?, val blockName: ExerciseBlock?)

fun NavController.navigateToSpeakingExercise(exerciseId: Long?, blockName: ExerciseBlock?) {
    navigate(route = SpeakingExerciseRoute(exerciseId, blockName)) {

    }
}

fun NavController.navigateToSpeakingExerciseWithBack(exerciseId: Long?, blockName: ExerciseBlock?) {
    navigate(route = SpeakingExerciseRoute(exerciseId, blockName)) {
        navOptions {
            popBackStack()
        }
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