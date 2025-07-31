package com.korop.yaroslavhorach.block_is_locked.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.block_is_locked.BlockIsLockedRoute
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import kotlinx.serialization.Serializable

@Serializable
data class BlockIsLockedNavigation(val block: ExerciseBlock)

fun NavController.navigateToBlockIsLocked(block: ExerciseBlock) {
    navigate(route = BlockIsLockedNavigation(block)) {
        navOptions {}
    }
}

fun NavGraphBuilder.blockIsLockedScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPremium: () -> Unit,
) {
    composable<BlockIsLockedNavigation> {
        BlockIsLockedRoute(
            onNavigateBack = onNavigateBack,
            onGoToPremium = onNavigateToPremium
        )
    }
}