package com.korop.yaroslavhorach.home.model

import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.Skill
import com.korop.yaroslavhorach.home.R

class ExerciseUi(
    val exercise: Exercise
) {
    val progressPercent = (exercise.exerciseProgress.progress.toFloat() / exercise.exerciseProgress.maxProgress.toFloat()).coerceAtMost(1f)
    val progress = exercise.exerciseProgress.progress.toString() + "/" + exercise.exerciseProgress.maxProgress.toString()
    val isStarted: Boolean = exercise.exerciseProgress.progress > 0
    val isLastActive: Boolean = exercise.isLastActive
    val isEnable: Boolean = exercise.isEnable
    val isFinished: Boolean = exercise.exerciseProgress.isFinished
    val isInProgress: Boolean = isStarted && isFinished.not()

    val skillNameResId: Int = when (exercise.skill) {
        Skill.COMMUNICATION -> R.string.communication_skills_exercise_section_name
        Skill.VOCABULARY -> R.string.vocabulary_practice_exercise_section_name
        Skill.DICTION -> R.string.diction_practice_exercise_section_name
    }

    val iconResId: Int = when (exercise.skill) {
        Skill.COMMUNICATION -> com.korop.yaroslavhorach.designsystem.R.drawable.ic_microphone
        Skill.VOCABULARY -> com.korop.yaroslavhorach.designsystem.R.drawable.ic_book
        Skill.DICTION -> com.korop.yaroslavhorach.designsystem.R.drawable.ic_tongue
    }
}