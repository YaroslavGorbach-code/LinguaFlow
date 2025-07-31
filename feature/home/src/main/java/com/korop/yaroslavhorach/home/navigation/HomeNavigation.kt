package com.korop.yaroslavhorach.home.navigation

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToExercise: (Exercise) -> Unit,
    onNavigateToAvatarChange: () -> Unit,
    onNavigateToBlockRepeat: (block: ExerciseBlock) -> Unit,
    onNavigateToBlockIsLocked: (block: ExerciseBlock) -> Unit,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit
) {
    composable<HomeRoute> {
        HomeRoute(
            onNavigateToExercise,
            onNavigateToAvatarChange,
            onNavigateToBlockRepeat,
            onNavigateToBlockIsLocked,
            onChangeColorScheme
        )
    }
}
