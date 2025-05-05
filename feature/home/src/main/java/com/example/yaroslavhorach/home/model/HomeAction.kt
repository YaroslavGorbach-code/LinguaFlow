package com.example.yaroslavhorach.home.model

sealed class HomeAction {
    class OnExerciseClicked(val exerciseUi: ExerciseUi): HomeAction()
}