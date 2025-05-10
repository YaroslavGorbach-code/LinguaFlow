package com.example.yaroslavhorach.home.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import com.example.yaroslavhorach.domain.exercise.model.Exercise

sealed class HomeAction {
    class OnStartExerciseClicked(val exercise: Exercise) : HomeAction()
    class OnExerciseClicked(val exerciseUi: ExerciseUi, val offSet: Offset): HomeAction()
    class OnShowStartExerciseTooltip(val position: Offset): HomeAction()
    class OnDescriptionBoundsChanged(val bounds: Rect): HomeAction()
    class OnTouchOutside(val position: Offset): HomeAction()
    class OnDescriptionListTopPaddingChanged(val padding: Dp): HomeAction()
    data object OnHideDescription : HomeAction()
}