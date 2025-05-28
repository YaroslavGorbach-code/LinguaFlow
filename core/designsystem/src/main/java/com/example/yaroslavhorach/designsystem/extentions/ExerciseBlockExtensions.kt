package com.example.yaroslavhorach.designsystem.extentions

import androidx.compose.ui.graphics.Color
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
        ExerciseBlock.ONE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
        ExerciseBlock.TWO -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_two
        ExerciseBlock.THREE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_three
        ExerciseBlock.FOUR -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_four
        ExerciseBlock.FIVE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_five
        ExerciseBlock.SIX -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_block_one
    }

fun ExerciseBlock.blockTitle(): UiText.FromString {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromString("Блок 1: Small Talk & Знайомство")
        ExerciseBlock.TWO -> UiText.FromString("Блок 2: Особисті теми & Повсякденне життя")
        ExerciseBlock.THREE -> UiText.FromString("Блок 3: Соціальні ситуації & Групова розмова")
        ExerciseBlock.FOUR -> UiText.FromString("Блок 4: Аргументація & М’які суперечки")
        ExerciseBlock.FIVE -> UiText.FromString("Блок 5: Емпатія & Активне слухання")
        ExerciseBlock.SIX -> TODO()
    }
}

fun ExerciseBlock.blockDescription(): UiText.FromString {
    return when (this) {
        ExerciseBlock.ONE -> UiText.FromString("Навчись легко починати розмову, підтримувати бесіду і знайомитись з новими людьми")
        ExerciseBlock.TWO -> UiText.FromString("Розвивай впевненність в розмовах про себе, свої інтереси, думки, почуття")
        ExerciseBlock.THREE -> UiText.FromString("Навчися не губитися серед людей, підтримувати темп розмови й знаходити свій голос у будь-якій компанії")
        ExerciseBlock.FOUR -> UiText.FromString("Тренуйся ввічливо висловлювати свою думку та аргументувати позицію")
        ExerciseBlock.FIVE -> UiText.FromString("Тренуй здатність відчувати емоції інших і виражати підтримку словами з теплом і турботою")
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