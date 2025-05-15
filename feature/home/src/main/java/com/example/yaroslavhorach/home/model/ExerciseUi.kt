package com.example.yaroslavhorach.home.model

import androidx.compose.ui.graphics.Color
import com.example.yaroslavhorach.designsystem.theme.OrangeDark
import com.example.yaroslavhorach.designsystem.theme.OrangeLight
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.Skill
import com.example.yaroslavhorach.home.R

class ExerciseUi(val exercise: Exercise) {
    val progressPercent = exercise.exerciseProgress.progress.toFloat() / exercise.exerciseProgress.maxProgress.toFloat()
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

    val colorLight: Color = when (exercise.block) {
        ExerciseBlock.ONE -> OrangeLight
        ExerciseBlock.TWO -> OrangeLight
        ExerciseBlock.THREE -> OrangeLight
        ExerciseBlock.FOUR -> OrangeLight
        ExerciseBlock.FIVE -> OrangeLight
        ExerciseBlock.SIX -> OrangeLight
    }

    val colorDark: Color = when (exercise.block) {
        ExerciseBlock.ONE -> OrangeDark
        ExerciseBlock.TWO -> OrangeDark
        ExerciseBlock.THREE -> OrangeDark
        ExerciseBlock.FOUR -> OrangeDark
        ExerciseBlock.FIVE -> OrangeDark
        ExerciseBlock.SIX -> OrangeDark
    }
}