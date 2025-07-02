package com.example.yaroslavhorach.avatar_change.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.yaroslavhorach.avatar_change.AvatarChangeRoute
import com.example.yaroslavhorach.avatar_change.AvatarChangeScreen
import com.example.yaroslavhorach.profile.ProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data object AvatarChange

fun NavController.navigateToAvatarChange(navOptions: NavOptions? = null) {
    this.navigate(AvatarChange, navOptions)
}

fun NavGraphBuilder.avatarChangeScreen(
    navigateBack: () -> Unit,
    navigateToPremium: () -> Unit,
    navigateHome: () -> Unit
) {
    composable<AvatarChange> {
        AvatarChangeRoute(
            navigateBack = navigateBack,
            navigateToPremium = navigateToPremium,
            navigateHome = navigateHome
        )
    }
}
