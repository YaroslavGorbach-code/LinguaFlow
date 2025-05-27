package com.example.yaroslavhorach.home.navigation

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.home.HomeRoute

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToExercise: (Exercise) -> Unit,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit
) {
    composable(route = homeNavigationRoute) {
        HomeRoute(onNavigateToExercise, onChangeColorScheme)
    }
}
