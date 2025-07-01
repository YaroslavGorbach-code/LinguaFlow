package com.example.yaroslavhorach.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.utill.combine
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.game.model.Challenge
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.home.model.GameSort
import com.example.yaroslavhorach.home.model.GameUi
import com.example.yaroslavhorach.home.model.GamesAction
import com.example.yaroslavhorach.home.model.GamesUiMessage
import com.example.yaroslavhorach.home.model.GamesViewState
import com.example.yaroslavhorach.home.model.getPermanentSorts
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
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    prefsRepository: PrefsRepository
) : BaseViewModel<GamesViewState, GamesAction, GamesUiMessage>() {

    override val pendingActions: MutableSharedFlow<GamesAction> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val games: MutableStateFlow<List<GameUi>> = MutableStateFlow(emptyList())
    private val selectedSort: MutableStateFlow<GameSort?> = MutableStateFlow(null)

    override val state: StateFlow<GamesViewState> = combine(
        prefsRepository.getUserData(),
        games,
        gameRepository.getChallenge(),
        selectedSort,
        prefsRepository.getFavoriteGamesIds(),
        uiMessageManager.message
    ) { userData, games, challenge, selectedSort, favorites, messages ->
        GamesViewState(
            sorts = getPermanentSorts().toMutableList().apply {
                if (favorites.isEmpty().not()) {
                    add(0, GameSort.FAVORITE)
                }
                if (challenge.status.started && challenge.status.completed.not()) {
                    add(0, GameSort.DAILY_CHALLENGE)
                }
            },
            challenge = challenge,
            favorites = favorites,
            selectedSort = selectedSort,
            availableTokens = userData.availableTokens,
            maxTokens = userData.maxTokens,
            games = filterGames(games, selectedSort, favorites, challenge)
                .sortedBy { it.game.minExperienceRequired },
            experience = userData.experience,
            isUserPremium = userData.isPremium,
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
                    is GamesAction.OnStartDailyChallengeClicked -> {
                        if (state.value.availableTokens > 0) {
                            gameRepository.startDailyChallenge()
                            prefsRepository.useToken()
                        }
                    }
                    is GamesAction.OnGameClicked -> {
                        changeDescriptionState(event.gameUi)
                    }
                    is GamesAction.OnStartGameClicked -> {
                        Log.v("dsasd", "dd")
                        if (event.useToken) {
                            prefsRepository.useToken()
                        }
                    }
                    is GamesAction.OnSortSelected -> {
                        if (selectedSort.value == event.item) {
                            selectedSort.value = null
                        } else {
                            selectedSort.value = event.item
                        }
                    }
                    is GamesAction.OnAddToFavoritesClicked -> {
                        prefsRepository.addGameToFavorites(event.game.game.id)
                    }
                    is GamesAction.OnRemoveFavoritesClicked -> {
                        if (selectedSort.value == GameSort.FAVORITE && event.game.isDescriptionVisible) {
                            changeDescriptionState(event.game)
                        }

                        prefsRepository.removeGameFromFavorites(event.game.game.id)
                    }
                    is GamesAction.OnGoToDailyChallengeExercises -> {
                        selectedSort.value = GameSort.DAILY_CHALLENGE
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    private fun changeDescriptionState(param: GameUi) {
        games.update { gameList ->
            gameList.map { gameUi ->
                if (gameUi.game.id == param.game.id) {
                    gameUi.copy(isDescriptionVisible = gameUi.isDescriptionVisible.not())
                } else {
                    gameUi.copy(isDescriptionVisible = false)
                }
            }
        }
    }

    private fun filterGames(
        games: List<GameUi>,
        selectedSort: GameSort?,
        favorites: List<Long>,
        challenge: Challenge
    ) = games.filter {
        when (selectedSort) {
            GameSort.DAILY_CHALLENGE -> it.game.skills.contains(challenge.theme)
            GameSort.FAVORITE -> favorites.contains(it.game.id)
            GameSort.CREATIVE -> it.game.skills.contains(com.example.yaroslavhorach.domain.game.model.Game.Skill.CREATIVE)
            GameSort.HUMOR -> it.game.skills.contains(com.example.yaroslavhorach.domain.game.model.Game.Skill.HUMOR)
            GameSort.STORYTELLING -> it.game.skills.contains(com.example.yaroslavhorach.domain.game.model.Game.Skill.STORYTELLING)
            GameSort.VOCABULARY -> it.game.skills.contains(com.example.yaroslavhorach.domain.game.model.Game.Skill.VOCABULARY)
            GameSort.DICTION -> it.game.skills.contains(com.example.yaroslavhorach.domain.game.model.Game.Skill.DICTION)
            GameSort.FLIRT -> it.game.skills.contains(com.example.yaroslavhorach.domain.game.model.Game.Skill.FLIRT)
            null -> true
        }
    }
}
