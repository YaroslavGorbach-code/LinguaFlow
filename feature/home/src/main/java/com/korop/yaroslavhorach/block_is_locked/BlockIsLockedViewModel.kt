package com.korop.yaroslavhorach.block_is_locked

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.block_is_locked.model.BlockIsLockedAction
import com.korop.yaroslavhorach.block_is_locked.model.BlockIsLockedUiiMessage
import com.korop.yaroslavhorach.block_is_locked.model.BlockIsLockedViewState
import com.korop.yaroslavhorach.block_is_locked.navigation.BlockIsLockedNavigation
import com.korop.yaroslavhorach.common.base.BaseViewModel
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
class BlockIsLockedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<BlockIsLockedViewState, BlockIsLockedAction, BlockIsLockedUiiMessage>() {

    override val pendingActions: MutableSharedFlow<BlockIsLockedAction> = MutableSharedFlow()

    private val block = savedStateHandle.toRoute<BlockIsLockedNavigation>().block

    override val state: StateFlow<BlockIsLockedViewState> = combine(
        flowOf(block),
        uiMessageManager.message,
    ) { game, message ->
        BlockIsLockedViewState(game, message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BlockIsLockedViewState.Empty
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
