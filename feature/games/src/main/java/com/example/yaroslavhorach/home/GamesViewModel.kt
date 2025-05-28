package com.example.yaroslavhorach.home

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.home.model.GamesAction
import com.example.yaroslavhorach.home.model.GamesUiMessage
import com.example.yaroslavhorach.home.model.GamesViewState
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
class GamesViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository
) : BaseViewModel<GamesViewState, GamesAction, GamesUiMessage>() {

    override val pendingActions: MutableSharedFlow<GamesAction> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val state: StateFlow<GamesViewState> = combine(
            exerciseRepository.getExercises(),
            uiMessageManager.message
        ) { exercises, messages ->
            GamesViewState(
                uiMessage = messages
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GamesViewState.Empty
        )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    GamesAction.OnStartDailyChallengeClicked -> TODO()
                }
            }
            .launchIn(viewModelScope)
    }
}
