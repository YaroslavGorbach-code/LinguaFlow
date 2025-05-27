package com.example.yaroslavhorach.designsystem.extentions

import androidx.compose.ui.graphics.Color
import com.example.yaroslavhorach.designsystem.theme.BlueDark
import com.example.yaroslavhorach.designsystem.theme.BlueLight
import com.example.yaroslavhorach.designsystem.theme.OrangeDark
import com.example.yaroslavhorach.designsystem.theme.OrangeLight
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.ui.UiText

val ExerciseBlock.topBarBgRes: Int
    get() = when (this) {
        ExerciseBlock.ONE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
        ExerciseBlock.TWO -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_two
        ExerciseBlock.THREE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
        ExerciseBlock.FOUR -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
        ExerciseBlock.FIVE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
        ExerciseBlock.SIX -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
    }

fun ExerciseBlock.blockTitle(): UiText.FromString {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromString("Блок 1: Small Talk & Знайомство")
        ExerciseBlock.TWO -> UiText.FromString("Блок 2: Особисті теми & Повсякденне життя")
        ExerciseBlock.THREE -> TODO()
        ExerciseBlock.FOUR -> TODO()
        ExerciseBlock.FIVE -> TODO()
        ExerciseBlock.SIX -> TODO()
    }
}

fun ExerciseBlock.blockDescription(): UiText.FromString {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromString("Навчись легко починати розмову, підтримувати бесіду і знайомитись з новими людьми")
        ExerciseBlock.TWO -> UiText.FromString("Розвивай впевненність в розмовах про себе, свої інтереси, думки, почуття")
        ExerciseBlock.THREE -> TODO()
        ExerciseBlock.FOUR -> TODO()
        ExerciseBlock.FIVE -> TODO()
        ExerciseBlock.SIX -> TODO()
    }
}

fun ExerciseBlock.blockColorPrimary(): Color {
    return when (this) {
        ExerciseBlock.ONE -> OrangeLight
        ExerciseBlock.TWO -> BlueLight
        ExerciseBlock.THREE -> OrangeLight
        ExerciseBlock.FOUR -> OrangeLight
        ExerciseBlock.FIVE -> OrangeLight
        ExerciseBlock.SIX -> OrangeLight
    }
}

fun ExerciseBlock.blockColorSecondary(): Color {
    return when (this) {
        ExerciseBlock.ONE -> OrangeDark
        ExerciseBlock.TWO -> BlueDark
        ExerciseBlock.THREE -> OrangeDark
        ExerciseBlock.FOUR -> OrangeDark
        ExerciseBlock.FIVE -> OrangeDark
        ExerciseBlock.SIX -> OrangeDark
    }
}