package com.korop.yaroslavhorach.block_is_locked.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock

data class BlockIsLockedViewState(
    val block: ExerciseBlock? = null,
    val uiMessage: UiMessage<BlockIsLockedUiiMessage>? = null
) {

    companion object {
        val Empty = BlockIsLockedViewState()
        val Preview = BlockIsLockedViewState(ExerciseBlock.ONE)
    }
}