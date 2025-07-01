package com.example.yaroslavhorach.designsystem.screens.premium.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.yaroslavhorach.designsystem.screens.premium.PremiumRoute
import kotlinx.serialization.Serializable

@Serializable
data object PremiumRoute

fun NavController.navigateToPremium() {
    navigate(route = PremiumRoute) {
        navOptions {}
    }
}

fun NavGraphBuilder.premiumScreen(onNavigateBack: () -> Unit, onNavigateToSuccess: ()-> Unit) {
    composable<PremiumRoute> {
        PremiumRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToSuccess = onNavigateToSuccess
        )
    }
}