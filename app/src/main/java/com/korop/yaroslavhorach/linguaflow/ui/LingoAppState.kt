package com.korop.yaroslavhorach.linguaflow.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.home.navigation.navigateToGames
import com.korop.yaroslavhorach.home.navigation.navigateToHome
import com.korop.yaroslavhorach.profile.navigation.navigateToProfile
import com.korop.yaroslavhorach.linguaflow.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberLingoAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): LingoAppState {
    return remember(navController, coroutineScope) {
        LingoAppState(navController, coroutineScope)
    }
}

@Stable
class LingoAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.route == topLevelDestination.route.qualifiedName
            }
        }

    val isBottomBarAndTopBarVisible: Boolean
        @Composable
        get() = TopLevelDestination.entries.map {
            it.route.qualifiedName }.contains(currentDestination?.route?.substringBefore("?")?.substringBefore("/"))

    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination.Home,
        TopLevelDestination.Games,
        TopLevelDestination.Profile
    )

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        if (navController.currentDestination?.route == topLevelDestination.route.qualifiedName) return

        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.Home -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.Games -> navController.navigateToGames(topLevelNavOptions)
            TopLevelDestination.Profile -> navController.navigateToProfile(topLevelNavOptions)
        }
    }
}