package com.example.yaroslavhorach.home.model

import androidx.compose.ui.graphics.Color
import com.example.yaroslavhorach.designsystem.theme.BlueDark
import com.example.yaroslavhorach.designsystem.theme.BlueLight
import com.example.yaroslavhorach.designsystem.theme.OrangeDark
import com.example.yaroslavhorach.designsystem.theme.OrangeLight
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.Skill
import com.example.yaroslavhorach.home.R
import com.example.yaroslavhorach.ui.UiText

class ExerciseUi(val exercise: Exercise) {
    val progressPercent = (exercise.exerciseProgress.progress.toFloat() / exercise.exerciseProgress.maxProgress.toFloat()).coerceAtMost(1f)
    val progress = exercise.exerciseProgress.progress.toString() + "/" + exercise.exerciseProgress.maxProgress.toString()
    val isStarted: Boolean = exercise.exerciseProgress.progress > 0
    val isLastActive: Boolean = exercise.isLastActive
    val isEnable: Boolean = exercise.isEnable

    val skillNameResId: Int = when (exercise.skill) {
        Skill.COMMUNICATION -> R.string.communication_skills_exercise_section_name
        Skill.VOCABULARY -> R.string.vocabulary_practice_exercise_section_name
        Skill.DICTION -> R.string.diction_practice_exercise_section_name
    }

    val iconResId: Int = when (exercise.skill) {
        Skill.COMMUNICATION -> LinguaIcons.Microphone
        Skill.VOCABULARY -> LinguaIcons.BlockWithLetters
        Skill.DICTION -> LinguaIcons.Tongue
    }
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