package com.example.yaroslavhorach.designsystem.screens.premium_success.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.yaroslavhorach.designsystem.screens.premium.PremiumRoute
import com.example.yaroslavhorach.designsystem.screens.premium_success.PremiumSuccessRoute
import kotlinx.serialization.Serializable

@Serializable
data object PremiumSuccessRoute

fun NavController.navigateToPremiumSuccess() {
    navigate(route = PremiumSuccessRoute) {
        navOptions {
            popBackStack()
        }
    }
}

fun NavGraphBuilder.premiumSuccessScreen(onNavigateBack: () -> Unit) {
    composable<PremiumSuccessRoute> {
        PremiumSuccessRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}