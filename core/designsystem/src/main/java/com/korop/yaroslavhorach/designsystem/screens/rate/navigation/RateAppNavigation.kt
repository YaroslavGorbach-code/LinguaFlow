package com.korop.yaroslavhorach.designsystem.screens.rate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.designsystem.screens.rate.RateAppRoute
import kotlinx.serialization.Serializable

@Serializable
data object RateAppNavigation

fun NavController.navigateToRateApp() {
    navigate(route = RateAppNavigation) {
        navOptions {
            popBackStack()
        }
    }
}

fun NavGraphBuilder.rateAppScreen(
    onNavigateBack: () -> Unit
) {
    composable<RateAppNavigation> {
        RateAppRoute(
            onNavigateBack = onNavigateBack
        )
    }
}