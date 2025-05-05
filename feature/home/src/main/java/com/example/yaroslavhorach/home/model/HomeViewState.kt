package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise.model.ExerciseProgress
import com.example.yaroslavhorach.domain.exercise.model.Skill

data class HomeViewState(
    val userName: String = "",
    val exercises: List<ExerciseUi> = emptyList(),
    val uiMessage: UiMessage<HomeUiMessage>? = null
) {
    companion object {
        val Empty = HomeViewState()
        val Preview = HomeViewState()
        val PreviewExercise = ExerciseUi(Exercise(
            id = ExerciseName.ICEBREAKERS.ordinal.toLong(),
            exerciseName = ExerciseName.ICEBREAKERS,
            skill = Skill.COMMUNICATION,
            exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
            block = ExerciseBlock.ONE
        ))
    }
}
