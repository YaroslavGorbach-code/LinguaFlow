package com.example.yaroslavhorach.home

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.home.model.ExerciseUi
import com.example.yaroslavhorach.home.model.HomeAction
import com.example.yaroslavhorach.home.model.HomeUiMessage
import com.example.yaroslavhorach.home.model.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
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
    exerciseRepository: ExerciseRepository,
    prefsRepository: PrefsRepository,
) : BaseViewModel<HomeViewState, HomeAction, HomeUiMessage>() {

    override val pendingActions: MutableSharedFlow<HomeAction> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val descriptionState: MutableStateFlow<HomeViewState.DescriptionState> = MutableStateFlow(
        HomeViewState.DescriptionState.EMPTY
    )

    override val state: StateFlow<HomeViewState> = com.example.yaroslavhorach.common.utill.combine(
            exerciseRepository.getExercises(),
            prefsRepository.getUserData(),
            descriptionState,
            exerciseRepository.getBlock(),
            uiMessageManager.message
        ) { exercises, userData, description, exercisesBlock, messages ->
            HomeViewState(
                uiMessage = messages,
                userName = userData.userName,
                descriptionState = description,
                exerciseBlock = exercisesBlock,
                exercises = exercises.map { ExerciseUi(it) }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeViewState.Empty
        )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    is HomeAction.OnExerciseClicked -> {
                        if (state.value.descriptionState.exercise == event.exerciseUi) {
                            descriptionState.value = HomeViewState.DescriptionState.EMPTY
                        } else {
                            descriptionState.value =
                                descriptionState.value.copy(
                                    exercise = event.exerciseUi,
                                    position = event.offSet
                                )
                        }
                    }
                    is HomeAction.OnHideDescription -> {
                        descriptionState.value = HomeViewState.DescriptionState.EMPTY
                    }
                    is HomeAction.OnDescriptionBoundsChanged -> {
                        descriptionState.value = descriptionState.value.copy(bounds = event.bounds)
                    }
                    is HomeAction.OnDescriptionListTopPaddingChanged -> {
                        descriptionState.value = descriptionState.value.copy(listTopExtraPadding = event.padding)
                    }
                    is HomeAction.OnTouchOutside -> {
                        state.value.descriptionState.bounds?.let { bounds ->
                            if (bounds
                                    .contains(event.position)
                                    .not()
                            ) {
                                descriptionState.value = HomeViewState.DescriptionState.EMPTY
                            }
                        }
                    }
                    is HomeAction.OnStartExerciseClicked -> {
                        descriptionState.value = HomeViewState.DescriptionState.EMPTY
                    }
                    is HomeAction.OnExercisesBlockChanged -> {
                        exerciseRepository.changeBlock(event.block)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
