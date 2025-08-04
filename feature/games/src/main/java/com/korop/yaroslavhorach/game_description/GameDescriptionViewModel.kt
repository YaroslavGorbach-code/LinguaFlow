package com.korop.yaroslavhorach.game_description

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.game_description.model.GameDescriptionAction
import com.korop.yaroslavhorach.game_description.model.GameDescriptionUiMessage
import com.korop.yaroslavhorach.game_description.model.GameDescriptionViewState
import com.korop.yaroslavhorach.game_description.model.GameUi
import com.korop.yaroslavhorach.game_description.navigation.GameDescriptionNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GameDescriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository,
    prefsRepository: PrefsRepository
) : BaseViewModel<GameDescriptionViewState, GameDescriptionAction, GameDescriptionUiMessage>() {
    override val pendingActions: MutableSharedFlow<GameDescriptionAction> = MutableSharedFlow()

    private val gameId = savedStateHandle.toRoute<GameDescriptionNavigation>().gameId
    private val useToken = savedStateHandle.toRoute<GameDescriptionNavigation>().useToken

    override val state: StateFlow<GameDescriptionViewState> = combine(
        flow { emit(gameRepository.getGame(gameId)) },
        flowOf(useToken),
        prefsRepository.getFavoriteGamesIds(),
        prefsRepository.getUserData(),
        uiMessageManager.message,
    ) { game, useToken, favorites, userData, message ->
        GameDescriptionViewState(
            game = game?.let { GameUi(it) },
            favorites = favorites,
            useToken = useToken,
            isUserPremium = userData.isPremium,
            experience = userData.experience,
            availableTokens = userData.availableTokens,
            uiMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameDescriptionViewState.Empty
    )

    init {
        pendingActions
            .onEach { event ->
                when (event) {
                    is GameDescriptionAction.OnAddToFavoritesClicked -> {
                        state.value.game?.game?.id?.let { prefsRepository.addGameToFavorites(it) }
                    }
                    is GameDescriptionAction.OnRemoveFavoritesClicked -> {
                        state.value.game?.game?.id?.let { prefsRepository.removeGameFromFavorites(it) }
                    }
                    is GameDescriptionAction.OnStartGameClicked -> {
                        if (state.value.useToken) {
                            prefsRepository.useToken()
                        }
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }
}
