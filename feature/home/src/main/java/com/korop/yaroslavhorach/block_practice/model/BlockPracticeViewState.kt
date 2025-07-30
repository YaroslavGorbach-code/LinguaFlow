package com.korop.yaroslavhorach.block_practice.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock

data class BlockPracticeViewState(
    val block: ExerciseBlock? = null,
    val stars: Int = 0,
    val uiMessage: UiMessage<BlockPracticeUiMessage>? = null
) {

    companion object {
        val Empty = BlockPracticeViewState()
        val Preview = BlockPracticeViewState(ExerciseBlock.ONE)
    }
}