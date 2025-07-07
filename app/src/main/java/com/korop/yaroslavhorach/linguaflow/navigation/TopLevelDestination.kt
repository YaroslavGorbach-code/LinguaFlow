package com.korop.yaroslavhorach.linguaflow.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.secondaryIcon
import com.korop.yaroslavhorach.home.navigation.GamesRoute
import com.korop.yaroslavhorach.home.navigation.HomeRoute
import com.korop.yaroslavhorach.lingoFlow.R
import com.korop.yaroslavhorach.profile.navigation.ProfileRoute
import kotlin.reflect.KClass

/**
 * Type for the top level destinations in the application.
 */
enum class TopLevelDestination(
    val iconResId: Int,
    val titleTextResId: Int,
    val route: KClass<*>
) {
    Home(LinguaIcons.Home, R.string.nav_bar_home_title, HomeRoute::class),
    Games(LinguaIcons.Game, R.string.nav_bar_calendar_title, GamesRoute::class),
    Profile(LinguaIcons.Profile, R.string.nav_bar_profile_title, ProfileRoute::class);

    @Composable
    fun getUnselectedIconColor(): Color = MaterialTheme.colorScheme.secondaryIcon()

    @Composable
    fun getSelectedIconColor(): Color = MaterialTheme.colorScheme.primary
}