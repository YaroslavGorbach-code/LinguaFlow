package com.example.yaroslavhorach.home.navigation

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToExercise: (Exercise) -> Unit,
    onNavigateToAvatarChange: () -> Unit,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit
) {
    composable<HomeRoute> {
        HomeRoute(onNavigateToExercise, onNavigateToAvatarChange, onChangeColorScheme)
    }
}
