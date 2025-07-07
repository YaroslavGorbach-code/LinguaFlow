package com.korop.yaroslavhorach.designsystem.screens.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.designsystem.screens.onboarding.OnboardingRoute
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