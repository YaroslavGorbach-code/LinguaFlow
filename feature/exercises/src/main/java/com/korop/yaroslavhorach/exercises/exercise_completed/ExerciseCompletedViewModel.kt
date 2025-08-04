package com.korop.yaroslavhorach.exercises.exercise_completed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.helpers.AdManager
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.common.utill.toMinutesSecondsFormat
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedAction
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedUiMessage
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedViewState
import com.korop.yaroslavhorach.exercises.exercise_completed.navigation.ExerciseCompletedRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseCompletedViewModel @Inject constructor(
    val adManager: AdManager,
    private val gamesRepository: GameRepository,
    savedStateHandle: SavedStateHandle,
    private val prefsRepository: PrefsRepository,
) : BaseViewModel<ExerciseCompletedViewState, ExerciseCompletedAction, ExerciseCompletedUiMessage>() {

    override val pendingActions: MutableSharedFlow<ExerciseCompletedAction> = MutableSharedFlow()

    private val lastUnlockedGame: MutableStateFlow<Game?> = MutableStateFlow(null)

   private val gameName = savedStateHandle.toRoute<ExerciseCompletedRoute>().completedGameName

    override val state: StateFlow<ExerciseCompletedViewState> = combine(
        prefsRepository.getUserData(),
        lastUnlockedGame,
        uiMessageManager.message
    ) { user, lastUnlockedGame, message ->
        ExerciseCompletedViewState(
            savedStateHandle.toRoute<ExerciseCompletedRoute>().time.toMinutesSecondsFormat(),
            savedStateHandle.toRoute<ExerciseCompletedRoute>().experience,
            user.experience,
            lastUnlockedGame?.id,
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
            gameName?.let {
                gamesRepository.markGameAsCompleted(it)
            }
        }

        gamesRepository.getLastUnlockedGame()
            .onEach {
                lastUnlockedGame.value = it
                it?.id?.let { it1 -> prefsRepository.markScreenGameUnlockedWasShown(it1) }
            }
            .launchIn(viewModelScope)

        pendingActions
            .onEach { event ->
                when (event) {
                    is ExerciseCompletedAction.OnContinueClicked -> {
                        state.value.lastUnlockedGameId?.let { id ->
                            uiMessageManager.emitMessage(UiMessage(ExerciseCompletedUiMessage.NavigateToGameUnlocked(id)))
                        } ?: run {
                            if (prefsRepository.getIsRateAppAllowed().first()) {
                                uiMessageManager.emitMessage(UiMessage(ExerciseCompletedUiMessage.NavigateToRateApp))
                            } else {
                                uiMessageManager.emitMessage(UiMessage(ExerciseCompletedUiMessage.ShowAd))
                            }
                        }
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
