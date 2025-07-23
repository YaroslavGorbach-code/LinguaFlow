package com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.helpers.AdManager
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model.GameUnlockedSuccessAction
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model.GameUnlockedSuccessUiMessage
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model.GameUnlockedSuccessViewState
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.navigation.GameUnlockedSuccessNavigation
import com.korop.yaroslavhorach.domain.game.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameUnlockedSuccessViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository,
    val adManager: AdManager
) : BaseViewModel<GameUnlockedSuccessViewState, GameUnlockedSuccessAction, GameUnlockedSuccessUiMessage>() {

    override val pendingActions: MutableSharedFlow<GameUnlockedSuccessAction> = MutableSharedFlow()

    private val gameId = savedStateHandle.toRoute<GameUnlockedSuccessNavigation>().gameId

    override val state: StateFlow<GameUnlockedSuccessViewState> = combine(
        flow{ emit(gameRepository.getGame(gameId)) },
        uiMessageManager.message,
    ) { game, message ->
        GameUnlockedSuccessViewState(game, message)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameUnlockedSuccessViewState.Empty
    )

    init {
        viewModelScope.launch {
            gameRepository.clearLastUnlockedGame()
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    is GameUnlockedSuccessAction.OnContinueBtnClicked -> {
                        uiMessageManager.emitMessage(UiMessage(GameUnlockedSuccessUiMessage.NavigateBack))
                    }
                    is GameUnlockedSuccessAction.OnTryGameBtnClicked -> {
                        uiMessageManager.emitMessage(UiMessage(GameUnlockedSuccessUiMessage.NavigateToGame(event.id)))
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
