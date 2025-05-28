package com.example.yaroslavhorach.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.home.GamesRoute

const val gamesNavigationRoute = "games_route"

fun NavController.navigateToGames(navOptions: NavOptions? = null) {
    this.navigate(gamesNavigationRoute, navOptions)
}

fun NavGraphBuilder.gamesScreen(
    onNavigateToExercise: (Exercise) -> Unit
) {
    composable(route = gamesNavigationRoute) {
        GamesRoute(onNavigateToExercise)
    }
}
