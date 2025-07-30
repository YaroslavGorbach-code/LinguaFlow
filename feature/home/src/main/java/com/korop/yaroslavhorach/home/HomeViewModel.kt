package com.korop.yaroslavhorach.home

import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.home.model.ExerciseUi
import com.korop.yaroslavhorach.home.model.HomeAction
import com.korop.yaroslavhorach.home.model.HomeUiMessage
import com.korop.yaroslavhorach.home.model.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    private val exercises: MutableStateFlow<List<ExerciseUi>> = MutableStateFlow(emptyList())

    override val state: StateFlow<HomeViewState> = com.korop.yaroslavhorach.common.utill.combine(
            exercises,
            prefsRepository.getUserData(),
            descriptionState,
            exerciseRepository.getBlock(),
            uiMessageManager.message
        ) { exercises, userData, description, exercisesBlock, messages ->
            HomeViewState(
                uiMessage = messages,
                userName = userData.userName,
                stars = userData.stars,
                userAvatar = userData.avatarResId,
                descriptionState = description,
                exerciseBlock = exercisesBlock,
                exercises = exercises
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HomeViewState.Empty
    )

    init {
        exerciseRepository.getExercises()
            .map { it.map(::ExerciseUi) }
            .onEach { ex ->
                exercises.value = ex

                val lastActiveIndex = ex.indexOfFirst { it.isLastActive }

                viewModelScope.launch {
                    if (lastActiveIndex > 0) {
                        uiMessageManager.emitMessage(UiMessage(HomeUiMessage.ScrollTo(lastActiveIndex)))
                    }
                }
            }
            .launchIn(viewModelScope)

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
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }
}
