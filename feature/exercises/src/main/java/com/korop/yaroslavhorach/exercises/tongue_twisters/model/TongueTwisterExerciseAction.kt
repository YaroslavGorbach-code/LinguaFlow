package com.korop.yaroslavhorach.exercises.tongue_twisters.model

sealed class TongueTwisterExerciseAction {
    data object OnNextClicked : TongueTwisterExerciseAction()
    data object OnBackClicked : TongueTwisterExerciseAction()
}