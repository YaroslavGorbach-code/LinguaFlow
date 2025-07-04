package com.example.yaroslavhorach.linguaflow.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.yaroslavhorach.designsystem.theme.components.LinguaNavigationBar
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.LinguaNavigationBarItem
import com.example.yaroslavhorach.linguaflow.navigation.LingoNavHost
import com.example.yaroslavhorach.linguaflow.navigation.TopLevelDestination

@Composable
fun LingoApp(
    isOnboarding: Boolean,
    appState: LingoAppState = rememberLingoAppState(),
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit
) {
    LinguaBackground {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {},
            bottomBar = {
                if (appState.isBottomBarAndTopBarVisible) {
                    LingoBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                }
            },
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                LingoNavHost(
                    navController = appState.navController,
                    isOnboarding = isOnboarding,
                    onChangeColorScheme = onChangeColorScheme
                )
            }
        }
    }
}

@Composable
private fun LingoBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    LinguaNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            AddNavBarItems(destination, selected, onNavigateToDestination)
        }
    }
}

@Composable
private fun RowScope.AddNavBarItems(
    destination: TopLevelDestination,
    selected: Boolean,
    onNavigateToDestination: (TopLevelDestination) -> Unit
) {
    LinguaNavigationBarItem(
        selected = selected,
        onClick = { onNavigateToDestination(destination) },
        icon = {
            if (selected) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = destination.iconResId),
                    contentDescription = null,
                    tint = destination.getSelectedIconColor()
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = destination.iconResId),
                    contentDescription = null,
                    tint = destination.getUnselectedIconColor()
                )
            }
        },
        label = {
            if (selected) {
                Text(stringResource(destination.titleTextResId), color = destination.getSelectedIconColor())
            } else {
                Text(stringResource(destination.titleTextResId), color = destination.getUnselectedIconColor())
            }
        },
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination): Boolean {
    return this?.hierarchy?.any {
        it.hasRoute(destination.route)
    } ?: false
}
