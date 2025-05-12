package com.example.yaroslavhorach.exercises.speaking.model

import com.example.yaroslavhorach.domain.exercise_content.model.Test

sealed class SpeakingExerciseAction {
    class OnVariantChosen(val variant: Test.Variant) : SpeakingExerciseAction()
    data object OnNextTestClicked : SpeakingExerciseAction()
    data object OnNextSituationClicked : SpeakingExerciseAction()
    data object OnTryAgainSituationClicked : SpeakingExerciseAction()
    data object OnStartSpikingClicked : SpeakingExerciseAction()
    data object OnCheckTestVariantClicked : SpeakingExerciseAction()
    data object OnBackClicked : SpeakingExerciseAction()
    data object OnPlayRecordClicked : SpeakingExerciseAction()
    data object OnPauseRecordClicked : SpeakingExerciseAction()
    data object OnStopSpeakingClicked : SpeakingExerciseAction()
}