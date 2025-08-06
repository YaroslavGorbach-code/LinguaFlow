package com.korop.yaroslavhorach.exercises.vocabulary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.exercises.vocabulary.VocabularyExerciseRoute
import kotlinx.serialization.Serializable

@Serializable
data class VocabularyRoute(val exerciseId: Long)

fun NavController.navigateToVocabularyExercise(exerciseId: Long, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = VocabularyRoute(exerciseId), builder)
}

fun NavGraphBuilder.vocabularyExerciseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseCompleted: (time: Long, experience: Int, gameName: Game.GameName) -> Unit
) {
    composable<VocabularyRoute> {
        VocabularyExerciseRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToExerciseResult = { time, xp, gameName ->
                onNavigateToExerciseCompleted(time, xp, gameName)
            })
    }
}