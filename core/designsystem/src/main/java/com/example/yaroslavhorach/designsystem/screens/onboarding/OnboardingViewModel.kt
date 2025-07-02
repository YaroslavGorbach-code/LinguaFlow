package com.example.yaroslavhorach.designsystem.screens.onboarding

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.designsystem.screens.onboarding.model.OnboardingAction
import com.example.yaroslavhorach.designsystem.screens.onboarding.model.OnboardingUiMessage
import com.example.yaroslavhorach.designsystem.screens.onboarding.model.OnboardingViewState
import com.example.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessAction
import com.example.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessUiMessage
import com.example.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessViewState
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
class OnboardingViewModel @Inject constructor() :
    BaseViewModel<OnboardingViewState, OnboardingAction, OnboardingUiMessage>() {

    override val pendingActions: MutableSharedFlow<OnboardingAction> = MutableSharedFlow()

    override val state: StateFlow<OnboardingViewState> = combine(
        uiMessageManager.message,
        uiMessageManager.message
    ) { _, message ->
        OnboardingViewState(message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnboardingViewState.Empty
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
