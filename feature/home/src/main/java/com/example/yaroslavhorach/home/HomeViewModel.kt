package com.example.yaroslavhorach.home

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.home.model.ExerciseUi
import com.example.yaroslavhorach.home.model.HomeAction
import com.example.yaroslavhorach.home.model.HomeUiMessage
import com.example.yaroslavhorach.home.model.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository
) : BaseViewModel<HomeViewState, HomeAction, HomeUiMessage>() {

    override val pendingActions: MutableSharedFlow<HomeAction> = MutableSharedFlow()

    override val state: StateFlow<HomeViewState> =
        combine(
            exerciseRepository.getExercises(),
            uiMessageManager.message
        ) { exercises, messages ->
            HomeViewState(uiMessage = messages, exercises = exercises.map {
                ExerciseUi(it)
            })
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeViewState.Empty
        )

    init {
        pendingActions
            .onEach {
                when (it) {
                    is HomeAction.OnExerciseClicked -> {
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
