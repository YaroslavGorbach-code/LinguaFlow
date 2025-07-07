package com.korop.yaroslavhorach.designsystem.screens.premium_success

import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessAction
import com.korop.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessUiMessage
import com.korop.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PremiumSuccessViewModel @Inject constructor() :
    BaseViewModel<PremiumSuccessViewState, PremiumSuccessAction, PremiumSuccessUiMessage>() {

    override val pendingActions: MutableSharedFlow<PremiumSuccessAction> = MutableSharedFlow()

    override val state: StateFlow<PremiumSuccessViewState> = combine(
        uiMessageManager.message,
        uiMessageManager.message
    ) { _, message ->
        PremiumSuccessViewState(message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PremiumSuccessViewState.Empty
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
