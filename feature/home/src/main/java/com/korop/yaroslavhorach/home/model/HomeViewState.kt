package com.korop.yaroslavhorach.home.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseName
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseProgress
import com.korop.yaroslavhorach.domain.exercise.model.Skill
import com.korop.yaroslavhorach.home.R

data class HomeViewState(
    val userName: String = "",
    val userAvatar: Int? = null,
    val exercises: List<ExerciseUi> = emptyList(),
    val descriptionState: DescriptionState = DescriptionState(),
    val exerciseBlock: ExerciseBlock = ExerciseBlock.ONE,
    val uiMessage: UiMessage<HomeUiMessage>? = null
) {
    val blockProgress: Float
        get() {
            val groupedExercises = exercises.groupBy { it.exercise.block }
            val blockExercises = groupedExercises[exerciseBlock].orEmpty()
            if (blockExercises.isEmpty()) return 0f

            val totalProgress = blockExercises.sumOf { it.progressPercent.toDouble() }

            return (totalProgress / blockExercises.size).toFloat()
        }

    data class DescriptionState(
        val listTopExtraPadding: Dp = 0.dp,
        val exercise: ExerciseUi? = null,
        val position: Offset? = null,
        val bounds: Rect? = null,
    ) {
        val isVisible
            get() = exercise != null

        companion object {
            val EMPTY = DescriptionState()
        }
    }

    companion object {
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

        val Empty = HomeViewState()
        val Preview =
            HomeViewState(
                exercises = listOf(PreviewExercise, PreviewExercise, PreviewExercise, PreviewExercise),
                userAvatar = com.korop.yaroslavhorach.designsystem.R.drawable.im_avatar_1
            )
    }
}
