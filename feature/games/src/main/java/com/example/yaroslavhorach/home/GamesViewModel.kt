package com.example.yaroslavhorach.home

import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.home.model.GameUi
import com.example.yaroslavhorach.home.model.GamesAction
import com.example.yaroslavhorach.home.model.GamesUiMessage
import com.example.yaroslavhorach.home.model.GamesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    gameRepository: GameRepository,
    prefsRepository: PrefsRepository
) : BaseViewModel<GamesViewState, GamesAction, GamesUiMessage>() {

    override val pendingActions: MutableSharedFlow<GamesAction> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val games: MutableStateFlow<List<GameUi>> = MutableStateFlow(emptyList())

    override val state: StateFlow<GamesViewState> = combine(
        prefsRepository.getUserData(),
        games,
        uiMessageManager.message
    ) { userData, games, messages ->
        GamesViewState(
            availableTokens = userData.availableTokens,
            maxTokens = userData.maxTokens,
            games = games,
            uiMessage = messages
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GamesViewState.Empty
    )

    init {
        gameRepository.getGames()
            .map { it.map(::GameUi) }
            .onEach { games.value = it }
            .launchIn(viewModelScope)

        pendingActions
            .onEach { event ->
                when (event) {
                    is GamesAction.OnStartDailyChallengeClicked -> TODO()
                    is GamesAction.OnGameClicked -> {
                        games.update { gameList ->
                            gameList.map { gameUi ->
                                if (gameUi.game.id == event.gameUi.game.id) {
                                    gameUi.copy(isDescriptionVisible = gameUi.isDescriptionVisible.not())
                                } else {
                                    gameUi.copy(isDescriptionVisible = false)
                                }
                            }
                        }
                    }
                    is GamesAction.OnStartGameClicked -> {
                        prefsRepository.useToken()
                    }
                    is GamesAction.OnPremiumBtnClicked -> {

                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
