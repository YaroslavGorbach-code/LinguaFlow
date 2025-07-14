package com.korop.yaroslavhorach.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.settings.SettingsRoute
import kotlinx.serialization.Serializable

@Serializable
data object Settings

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(Settings, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    navigateBack: () -> Unit
) {
    composable<Settings> {
        SettingsRoute(navigateBack = navigateBack)
    }
}
