package com.example.yaroslavhorach.exercises.exercise_completed.model

import com.example.yaroslavhorach.common.utill.UiMessage

data class ExerciseCompletedViewState(
    val time: String = "",
    val experience: Int = 0,
    val uiMessage: UiMessage<ExerciseCompletedUiMessage>? = null
) {

    companion object {
        val Empty = ExerciseCompletedViewState()
        val Preview = ExerciseCompletedViewState(
            "2:15", 10
        )
    }
}
