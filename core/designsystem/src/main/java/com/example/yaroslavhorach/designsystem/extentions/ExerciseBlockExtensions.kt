package com.example.yaroslavhorach.designsystem.extentions

import androidx.compose.ui.graphics.Color
import com.example.yaroslavhorach.designsystem.R
import com.example.yaroslavhorach.designsystem.theme.BlockFiveDark
import com.example.yaroslavhorach.designsystem.theme.BlockFiveLight
import com.example.yaroslavhorach.designsystem.theme.BlockTwoDark
import com.example.yaroslavhorach.designsystem.theme.BlockTwoLight
import com.example.yaroslavhorach.designsystem.theme.BlockFourDark
import com.example.yaroslavhorach.designsystem.theme.BlockFourLight
import com.example.yaroslavhorach.designsystem.theme.BlockOneDark
import com.example.yaroslavhorach.designsystem.theme.BlockOneLight
import com.example.yaroslavhorach.designsystem.theme.BlockThreeDark
import com.example.yaroslavhorach.designsystem.theme.BlockThreeLight
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.ui.UiText

val ExerciseBlock.topBarBgRes: Int
    get() = when (this) {
        ExerciseBlock.ONE -> R.drawable.gradient_block_one
        ExerciseBlock.TWO -> R.drawable.gradient_block_two
        ExerciseBlock.THREE -> R.drawable.gradient_block_three
        ExerciseBlock.FOUR -> R.drawable.gradient_block_four
        ExerciseBlock.FIVE -> R.drawable.gradient_block_five
        ExerciseBlock.SIX -> R.drawable.gradient_block_one
    }

fun ExerciseBlock.blockTitle(): UiText {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromResource(R.string.block_1_small_talk_title_text)
        ExerciseBlock.TWO -> UiText.FromResource(R.string.block_2_title_text)
        ExerciseBlock.THREE -> UiText.FromResource(R.string.block_3_title_text)
        ExerciseBlock.FOUR -> UiText.FromResource(R.string.block_4_title_text)
        ExerciseBlock.FIVE -> UiText.FromResource(R.string.block_5_title_text)
        ExerciseBlock.SIX -> TODO()
    }
}

fun ExerciseBlock.blockDescription(): UiText {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromResource(R.string.block_1_subtitle_text)
        ExerciseBlock.TWO -> UiText.FromResource(R.string.block_2_subtitle_text)
        ExerciseBlock.THREE -> UiText.FromResource(R.string.block_3_subtitle_text)
        ExerciseBlock.FOUR -> UiText.FromResource(R.string.block_4_subtitle_text)
        ExerciseBlock.FIVE -> UiText.FromResource(R.string.block_5_subtitle_text)
        ExerciseBlock.SIX -> TODO()
    }
}

fun ExerciseBlock.blockColorPrimary(): Color {
    return when (this) {
        ExerciseBlock.ONE -> BlockOneLight
        ExerciseBlock.TWO -> BlockTwoLight
        ExerciseBlock.THREE -> BlockThreeLight
        ExerciseBlock.FOUR -> BlockFourLight
        ExerciseBlock.FIVE -> BlockFiveLight
        ExerciseBlock.SIX -> BlockOneLight
    }
}

fun ExerciseBlock.blockColorSecondary(): Color {
    return when (this) {
        ExerciseBlock.ONE -> BlockOneDark
        ExerciseBlock.TWO -> BlockTwoDark
        ExerciseBlock.THREE -> BlockThreeDark
        ExerciseBlock.FOUR -> BlockFourDark
        ExerciseBlock.FIVE -> BlockFiveDark
        ExerciseBlock.SIX -> BlockOneDark
    }
}