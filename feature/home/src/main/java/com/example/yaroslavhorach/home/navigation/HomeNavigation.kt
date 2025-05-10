package com.example.yaroslavhorach.home.navigation

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

fun NavGraphBuilder.homeScreen(onNavigateToExercise: (Exercise) -> Unit) {
    composable(route = homeNavigationRoute) {
        HomeRoute(onNavigateToExercise)
    }
}
