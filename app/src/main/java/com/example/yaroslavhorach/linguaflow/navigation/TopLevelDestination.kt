package com.example.yaroslavhorach.linguaflow.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.designsystem.theme.secondaryIcon
import com.example.yaroslavhorach.home.navigation.gamesNavigationRoute
import com.example.yaroslavhorach.home.navigation.homeNavigationRoute
import com.example.yaroslavhorach.lingoFlow.R

/**
 * Type for the top level destinations in the application.
 */
sealed class TopLevelDestination(
    val iconResId: Int,
    val titleTextResId: Int,
    val navigationRoute: String?
) {
    @Composable
    fun getUnselectedIconColor(): Color = MaterialTheme.colorScheme.secondaryIcon()

    @Composable
    fun getSelectedIconColor(): Color = MaterialTheme.colorScheme.primary

    data object Home : TopLevelDestination(LinguaIcons.Home, R.string.nav_bar_home_title, homeNavigationRoute)

    data object Games : TopLevelDestination(LinguaIcons.Calendar, R.string.nav_bar_calendar_title, gamesNavigationRoute)

    data object Profile : TopLevelDestination(LinguaIcons.User, R.string.nav_bar_profile_title, null)
}