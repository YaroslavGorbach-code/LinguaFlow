package com.example.yaroslavhorach.home.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise.model.ExerciseProgress
import com.example.yaroslavhorach.domain.exercise.model.Skill

data class HomeViewState(
    val userName: String = "",
    val exercises: List<ExerciseUi> = emptyList(),
    val startExerciseTooltipPosition: Offset? = null,
    val descriptionState: DescriptionState = DescriptionState(),
    val uiMessage: UiMessage<HomeUiMessage>? = null
) {
    data class DescriptionState(
        val listTopExtraPadding: Dp = 0.dp,
        val exercise: ExerciseUi? = null,
        val position: Offset? = null,
        val bounds: Rect? = null,
    ){
        val isVisible
            get() = exercise != null

        companion object {
            val EMPTY = DescriptionState()
        }
    }

    companion object {
        val Empty = HomeViewState()
        val Preview = HomeViewState()
        val PreviewExercise = ExerciseUi(
            Exercise(
                id = ExerciseName.ICEBREAKERS.ordinal.toLong(),
                name = ExerciseName.ICEBREAKERS,
                skill = Skill.COMMUNICATION,
                exerciseProgress = ExerciseProgress(exerciseId = 1, progress = 0, maxProgress = 3),
                block = ExerciseBlock.ONE,
                nameString = hashMapOf("en" to "test"),
                description = hashMapOf("en" to "test")
            )
        )
    }
}
