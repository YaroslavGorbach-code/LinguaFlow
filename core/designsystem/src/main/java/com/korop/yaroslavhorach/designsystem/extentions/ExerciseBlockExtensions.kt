package com.korop.yaroslavhorach.designsystem.extentions

import androidx.compose.ui.graphics.Color
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.theme.BlockFiveDark
import com.korop.yaroslavhorach.designsystem.theme.BlockFiveLight
import com.korop.yaroslavhorach.designsystem.theme.BlockTwoDark
import com.korop.yaroslavhorach.designsystem.theme.BlockTwoLight
import com.korop.yaroslavhorach.designsystem.theme.BlockFourDark
import com.korop.yaroslavhorach.designsystem.theme.BlockFourLight
import com.korop.yaroslavhorach.designsystem.theme.BlockOneDark
import com.korop.yaroslavhorach.designsystem.theme.BlockOneLight
import com.korop.yaroslavhorach.designsystem.theme.BlockSevenDark
import com.korop.yaroslavhorach.designsystem.theme.BlockSevenLight
import com.korop.yaroslavhorach.designsystem.theme.BlockSixDark
import com.korop.yaroslavhorach.designsystem.theme.BlockSixLight
import com.korop.yaroslavhorach.designsystem.theme.BlockThreeDark
import com.korop.yaroslavhorach.designsystem.theme.BlockThreeLight
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.ui.UiText

val ExerciseBlock.topBarBgRes: Int
    get() = when (this) {
        ExerciseBlock.ONE -> R.drawable.gradient_block_one
        ExerciseBlock.TWO -> R.drawable.gradient_block_two
        ExerciseBlock.THREE -> R.drawable.gradient_block_three
        ExerciseBlock.FOUR -> R.drawable.gradient_block_four
        ExerciseBlock.FIVE -> R.drawable.gradient_block_five
        ExerciseBlock.SIX -> R.drawable.gradient_block_six
        ExerciseBlock.SEVEN -> R.drawable.gradient_block_seven
    }

fun ExerciseBlock.blockTitle(): UiText {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromResource(R.string.block_1_small_talk_title_text)
        ExerciseBlock.TWO -> UiText.FromResource(R.string.block_2_title_text)
        ExerciseBlock.THREE -> UiText.FromResource(R.string.block_3_title_text)
        ExerciseBlock.FOUR -> UiText.FromResource(R.string.block_4_title_text)
        ExerciseBlock.FIVE -> UiText.FromResource(R.string.block_5_title_text)
        ExerciseBlock.SIX ->  UiText.FromResource(R.string.block_6_title_text)
        ExerciseBlock.SEVEN ->  UiText.FromResource(R.string.block_7_title_text)
    }
}

fun ExerciseBlock.blockDescription(): UiText {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromResource(R.string.block_1_subtitle_text)
        ExerciseBlock.TWO -> UiText.FromResource(R.string.block_2_subtitle_text)
        ExerciseBlock.THREE -> UiText.FromResource(R.string.block_3_subtitle_text)
        ExerciseBlock.FOUR -> UiText.FromResource(R.string.block_4_subtitle_text)
        ExerciseBlock.FIVE -> UiText.FromResource(R.string.block_5_subtitle_text)
        ExerciseBlock.SIX -> UiText.FromResource(R.string.block_6_subtitle_text)
        ExerciseBlock.SEVEN -> UiText.FromResource(R.string.block_7_subtitle_text)
    }
}

fun ExerciseBlock.blockDoneDescription(): UiText {
    return UiText.FromResource(R.string.block_done_subtitle_text)
}

fun ExerciseBlock.blockPracticeMessage(): UiText {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromResource(R.string.block_practice_1_message_text)
        ExerciseBlock.TWO -> UiText.FromResource(R.string.block_practice_2_message_text)
        ExerciseBlock.THREE -> UiText.FromResource(R.string.block_practice_3_message_text)
        ExerciseBlock.FOUR -> UiText.FromResource(R.string.block_practice_4_message_text)
        ExerciseBlock.FIVE -> UiText.FromResource(R.string.block_practice_5_message_text)
        ExerciseBlock.SIX -> UiText.FromResource(R.string.block_practice_6_message_text)
        ExerciseBlock.SEVEN -> UiText.FromResource(R.string.block_practice_7_message_text)
    }
}

fun ExerciseBlock.blockColorPrimary(): Color {
    return when (this) {
        ExerciseBlock.ONE -> BlockOneLight
        ExerciseBlock.TWO -> BlockTwoLight
        ExerciseBlock.THREE -> BlockThreeLight
        ExerciseBlock.FOUR -> BlockFourLight
        ExerciseBlock.FIVE -> BlockFiveLight
        ExerciseBlock.SIX -> BlockSixLight
        ExerciseBlock.SEVEN -> BlockSevenLight
    }
}

fun ExerciseBlock.blockColorSecondary(): Color {
    return when (this) {
        ExerciseBlock.ONE -> BlockOneDark
        ExerciseBlock.TWO -> BlockTwoDark
        ExerciseBlock.THREE -> BlockThreeDark
        ExerciseBlock.FOUR -> BlockFourDark
        ExerciseBlock.FIVE -> BlockFiveDark
        ExerciseBlock.SIX -> BlockSixDark
        ExerciseBlock.SEVEN -> BlockSevenDark
    }
}