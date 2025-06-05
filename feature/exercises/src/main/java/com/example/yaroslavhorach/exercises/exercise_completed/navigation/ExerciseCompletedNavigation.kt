package com.example.yaroslavhorach.exercises.exercise_completed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.yaroslavhorach.exercises.exercise_completed.ExerciseCompletedRoute
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseCompletedRoute(val experience: Int, val time: Long)

fun NavController.navigateToExerciseCompleted(experience: Int, time: Long) {
    navigate(route = ExerciseCompletedRoute(experience, time)) {
        navOptions {
            popBackStack()
        }
    }
}

fun NavGraphBuilder.exerciseCompletedScreen(onNavigateBack: () -> Unit) {
    composable<ExerciseCompletedRoute> {
        ExerciseCompletedRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}