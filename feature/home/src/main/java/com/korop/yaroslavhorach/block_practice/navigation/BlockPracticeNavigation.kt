package com.korop.yaroslavhorach.block_practice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.block_practice.BlockPracticeRoute
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import kotlinx.serialization.Serializable

@Serializable
data class BlockPracticeNavigation(val block: ExerciseBlock)

fun NavController.navigateToBlockPractice(block: ExerciseBlock) {
    navigate(route = BlockPracticeNavigation(block)) {
        navOptions {
        }
    }
}

fun NavGraphBuilder.blockPracticeScreen(
    onNavigateBack: () -> Unit,
    onNavigateExercises: (blockName: ExerciseBlock) -> Unit,
) {
    composable<BlockPracticeNavigation> {
        BlockPracticeRoute(
            onNavigateBack = onNavigateBack,
            onNavigateExercises = onNavigateExercises
        )
    }
}