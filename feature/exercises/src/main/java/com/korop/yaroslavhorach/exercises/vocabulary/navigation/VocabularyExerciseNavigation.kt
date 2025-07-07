package com.korop.yaroslavhorach.exercises.vocabulary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.exercises.vocabulary.VocabularyExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class VocabularyRoute(val exerciseId: Long)

fun NavController.navigateToVocabularyExercise(exerciseId: Long) {
    navigate(route = VocabularyRoute(exerciseId)) {
        navOptions {

        }
    }
}

fun NavGraphBuilder.vocabularyExerciseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseCompleted: (time: Long, experience: Int) -> Unit
) {
    composable<VocabularyRoute> {
        VocabularyExerciseRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToExerciseResult = { time, xp ->
                onNavigateToExerciseCompleted(time, xp)
            })
    }
}