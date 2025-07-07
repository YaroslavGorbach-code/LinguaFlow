package com.korop.yaroslavhorach.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.korop.yaroslavhorach.profile.ProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(ProfileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(onNavigateToAvatarChange: ()-> Unit, onNavigateToPremium: ()-> Unit) {
    composable<ProfileRoute> {
        ProfileRoute(onNavigateToAvatarChange = onNavigateToAvatarChange, onNavigateToPremium = onNavigateToPremium)
    }
}
