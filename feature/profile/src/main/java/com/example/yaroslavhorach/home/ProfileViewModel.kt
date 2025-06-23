package com.example.yaroslavhorach.home

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.home.model.ProfileAction
import com.example.yaroslavhorach.home.model.ProfileUiMessage
import com.example.yaroslavhorach.home.model.ProfileViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel<ProfileViewState, ProfileAction, ProfileUiMessage>() {

    override val pendingActions: MutableSharedFlow<ProfileAction> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    override val state: StateFlow<ProfileViewState> = combine(
        uiMessageManager.message,
        uiMessageManager.message,
        ) { _, messages ->
            ProfileViewState(
                uiMessage = messages,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileViewState.Empty
        )

    init {
        pendingActions
            .onEach { event ->
                when (event) {

                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }
}
