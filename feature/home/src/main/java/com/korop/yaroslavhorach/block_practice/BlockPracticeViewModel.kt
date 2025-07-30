package com.korop.yaroslavhorach.block_practice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.block_practice.model.BlockPracticeAction
import com.korop.yaroslavhorach.block_practice.model.BlockPracticeUiMessage
import com.korop.yaroslavhorach.block_practice.model.BlockPracticeViewState
import com.korop.yaroslavhorach.block_practice.navigation.BlockPracticeNavigation
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BlockPracticeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    exerciseRepository: ExerciseRepository
) : BaseViewModel<BlockPracticeViewState, BlockPracticeAction, BlockPracticeUiMessage>() {

    override val pendingActions: MutableSharedFlow<BlockPracticeAction> = MutableSharedFlow()

    private val block = savedStateHandle.toRoute<BlockPracticeNavigation>().block

    override val state: StateFlow<BlockPracticeViewState> = combine(
        flowOf(block),
        exerciseRepository.getStarsForBlock(block),
        uiMessageManager.message,
    ) { game, stars, message ->
        BlockPracticeViewState(game, stars, message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BlockPracticeViewState.Empty
    )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
