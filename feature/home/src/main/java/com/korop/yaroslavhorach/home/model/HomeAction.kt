package com.korop.yaroslavhorach.home.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock

sealed class HomeAction {
    class OnStartExerciseClicked(val exercise: Exercise) : HomeAction()
    class OnExerciseClicked(val exerciseUi: ExerciseUi, val offSet: Offset): HomeAction()
    class OnDescriptionBoundsChanged(val bounds: Rect): HomeAction()
    class OnTouchOutside(val position: Offset): HomeAction()
    class OnDescriptionListTopPaddingChanged(val padding: Dp): HomeAction()
    class OnExercisesBlockChanged(val block: ExerciseBlock): HomeAction()
    data object OnHideDescription : HomeAction()
    data object OnAvatarClicked : HomeAction()
    data object OnRepeatBlockClicked: HomeAction()
    data object OnUnlockBlock: HomeAction()
}