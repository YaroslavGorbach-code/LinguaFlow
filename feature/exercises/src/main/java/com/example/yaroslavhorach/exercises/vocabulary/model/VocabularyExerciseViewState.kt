package com.example.yaroslavhorach.exercises.vocabulary.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise_content.model.Vocabulary

data class VocabularyExerciseViewState(
    val countdownValue: Int = 0,
    val wordsAmount: Int = 0,
    val vocabulary: Vocabulary? = null,
    val isExerciseActive: Boolean = false,
    val exerciseBlock: ExerciseBlock = ExerciseBlock.ONE,
    val uiMessage: UiMessage<VocabularyExerciseUiMessage>? = null
) {

    companion object {
        val Empty = VocabularyExerciseViewState()
        val Preview = VocabularyExerciseViewState()
    }
}
