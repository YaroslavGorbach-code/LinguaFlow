package com.example.yaroslavhorach.exercises.exercise_completed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.utill.toMinutesSecondsFormat
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedAction
import com.example.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedUiMessage
import com.example.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedViewState
import com.example.yaroslavhorach.exercises.exercise_completed.navigation.ExerciseCompletedRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseCompletedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefsRepository: PrefsRepository
) :
    BaseViewModel<ExerciseCompletedViewState, ExerciseCompletedAction, ExerciseCompletedUiMessage>() {

    override val pendingActions: MutableSharedFlow<ExerciseCompletedAction> = MutableSharedFlow()

    override val state: StateFlow<ExerciseCompletedViewState> = combine(
        prefsRepository.getUserData(),
        uiMessageManager.message
    ) { user, message ->
        ExerciseCompletedViewState(
            savedStateHandle.toRoute<ExerciseCompletedRoute>().time.toMinutesSecondsFormat(),
            savedStateHandle.toRoute<ExerciseCompletedRoute>().experience,
            user.experience,
            message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExerciseCompletedViewState.Empty
    )

    init {
        viewModelScope.launch {
            prefsRepository.addExperience(savedStateHandle.toRoute<ExerciseCompletedRoute>().experience)
            prefsRepository.markCurrentDayAsActive()
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
