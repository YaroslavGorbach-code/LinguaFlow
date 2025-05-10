package com.example.yaroslavhorach.exercises.speaking.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.yaroslavhorach.exercises.speaking.SpeakingExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class SpeakingExerciseRoute(val exerciseId: Long)

fun NavController.navigateToSpeakingExercise(exerciseId: Long) {
    navigate(route = SpeakingExerciseRoute(exerciseId)) {
        navOptions {

        }
    }
}

fun NavGraphBuilder.speakingExerciseScreen() {
    composable<SpeakingExerciseRoute> {
        SpeakingExerciseRoute()
    }
}