package com.example.yaroslavhorach.designsystem.screens.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.yaroslavhorach.designsystem.screens.onboarding.OnboardingRoute
import com.example.yaroslavhorach.designsystem.screens.premium.PremiumRoute
import com.example.yaroslavhorach.designsystem.screens.premium_success.PremiumSuccessRoute
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

fun NavController.navigateToOnboarding() {
    navigate(route = OnboardingRoute) {
        navOptions { }
    }
}

fun NavGraphBuilder.onboardingScreen(onNavigateToAvatarChange: () -> Unit) {
    composable<OnboardingRoute> {
        OnboardingRoute(onNavigateToAvatarChange = onNavigateToAvatarChange)
    }
}