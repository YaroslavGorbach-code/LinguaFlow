package com.korop.yaroslavhorach.designsystem.screens.rate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.designsystem.screens.rate.model.RateAppAction
import com.korop.yaroslavhorach.designsystem.screens.rate.model.RateAppUiMessage
import com.korop.yaroslavhorach.designsystem.screens.rate.model.RateAppViewState
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
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
class RateAppViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val prefsReposRepository: PrefsRepository
) : BaseViewModel<RateAppViewState, RateAppAction, RateAppUiMessage>() {

    override val pendingActions: MutableSharedFlow<RateAppAction> = MutableSharedFlow()

    override val state: StateFlow<RateAppViewState> = combine(
        uiMessageManager.message,
        uiMessageManager.message,
    ) { _, message ->
        RateAppViewState(message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RateAppViewState.Empty
    )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    is RateAppAction.OnRateClicked -> {
                        prefsReposRepository.markAppAsRated()
                        uiMessageManager.emitMessage(UiMessage(RateAppUiMessage.RateApp))
                    }
                    is RateAppAction.OnLetterClicked -> {
                        prefsReposRepository.markRateDelayed()
                        uiMessageManager.emitMessage(UiMessage(RateAppUiMessage.NavigateBack))
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
