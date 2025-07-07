package com.korop.yaroslavhorach.exercises.exercise_completed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.utill.toMinutesSecondsFormat
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedAction
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedUiMessage
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedViewState
import com.korop.yaroslavhorach.exercises.exercise_completed.navigation.ExerciseCompletedRoute
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
