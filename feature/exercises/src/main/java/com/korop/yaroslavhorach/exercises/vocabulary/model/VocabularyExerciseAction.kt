package com.korop.yaroslavhorach.exercises.vocabulary.model

sealed class VocabularyExerciseAction {
    data object OnStartClicked : VocabularyExerciseAction()
    data object OnTimerFinished : VocabularyExerciseAction()
    data object OnScreenClicked : VocabularyExerciseAction()
    data object OnTryAgainClicked : VocabularyExerciseAction()
    data object OnNextClicked : VocabularyExerciseAction()
    data object OnBackClicked : VocabularyExerciseAction()
}