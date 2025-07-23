package com.korop.yaroslavhorach.home

import android.icu.util.Calendar
import androidx.lifecycle.viewModelScope
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.common.utill.combine
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Challenge
import com.korop.yaroslavhorach.domain.game.model.ChallengeExerciseMix
import com.korop.yaroslavhorach.domain.game.model.ChallengeTimeLimited
import com.korop.yaroslavhorach.domain.holders.OpenGameDetailsHolder
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.home.model.GameSort
import com.korop.yaroslavhorach.home.model.GameUi
import com.korop.yaroslavhorach.home.model.GamesAction
import com.korop.yaroslavhorach.home.model.GamesUiMessage
import com.korop.yaroslavhorach.home.model.GamesViewState
import com.korop.yaroslavhorach.home.model.getPermanentSorts
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
import kotlinx.coroutines.time.delay
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
        gameRepository.getTodayChallenge(),
        selectedSort,
        prefsRepository.getFavoriteGamesIds(),
        uiMessageManager.message
    ) { userData, games, todayChallenge, selectedSort, favorites, messages ->
        val sorts = getPermanentSorts().toMutableList().apply {
            if (favorites.isEmpty().not()) {
                add(0, GameSort.FAVORITE)
            }
            if (todayChallenge.status.started && todayChallenge.status.completed.not()) {
                add(0, GameSort.DAILY_CHALLENGE)
            } else {
                if (selectedSort == GameSort.DAILY_CHALLENGE) {
                    this@GamesViewModel.selectedSort.value = null
                }
            }
        }

        GamesViewState(
            sorts = sorts,
            challenge = todayChallenge,
            favorites = favorites,
            selectedSort = selectedSort,
            availableTokens = userData.availableTokens,
            maxTokens = userData.maxTokens,
            allGames =  filterGames(games, null, favorites, todayChallenge),
            gamesForDisplay = filterGames(games, selectedSort, favorites, todayChallenge)
                .sortedWith(
                    compareBy(
                        { game ->
                            if (todayChallenge.status.inProgress) {
                                when {
                                    game.isChallengeGame && !game.isChallengeGameCompleted -> 0
                                    game.isChallengeGame && game.isChallengeGameCompleted -> 1
                                    else -> 2
                                }
                            } else {
                                2
                            }
                        },
                        { it.game.minExperienceRequired }
                    )
                ),
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
        OpenGameDetailsHolder.gameIdToOpen
            .onEach {
                if (it != null) {
                    kotlinx.coroutines.delay(500)
                    changeDescriptionState(it)
                    uiMessageManager.emitMessage(UiMessage(GamesUiMessage.ScrollToAndShowDescription(it)))
                    OpenGameDetailsHolder.gameIdToOpen.value = null
                }
            }
            .launchIn(viewModelScope)

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
                        changeDescriptionState(event.gameUi.game.id)
                    }
                    is GamesAction.OnStartGameClicked -> {
                        if (event.useToken) {
                            if ((state.value.availableTokens > 0)){
                                uiMessageManager.emitMessage(
                                    UiMessage(
                                        GamesUiMessage.NavigateToGame(
                                            event.gameUi.game.id,
                                            event.gameUi.game.name
                                        )
                                    )
                                )
                            }
                            prefsRepository.useToken()
                        } else {
                            uiMessageManager.emitMessage(
                                UiMessage(
                                    GamesUiMessage.NavigateToGame(
                                        event.gameUi.game.id,
                                        event.gameUi.game.name
                                    )
                                )
                            )
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
                            changeDescriptionState(event.game.game.id)
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

    private fun changeDescriptionState(id: Long) {
        games.update { gameList ->
            gameList.map { gameUi ->
                if (gameUi.game.id == id) {
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
    ) = games.filter { game ->
        when (selectedSort) {
            GameSort.DAILY_CHALLENGE -> {
                when (challenge) {
                    is ChallengeExerciseMix -> {
                        challenge.exercisesAndCompletedMark.any { it.first.name == game.game.name.name }
                    }
                    is ChallengeTimeLimited -> {
                        game.game.skills.contains(challenge.theme)
                    }
                    else -> true
                }
            }
            GameSort.FAVORITE -> favorites.contains(game.game.id)
            GameSort.CREATIVE -> game.game.skills.contains(com.korop.yaroslavhorach.domain.game.model.Game.Skill.CREATIVE)
            GameSort.HUMOR -> game.game.skills.contains(com.korop.yaroslavhorach.domain.game.model.Game.Skill.HUMOR)
            GameSort.STORYTELLING -> game.game.skills.contains(com.korop.yaroslavhorach.domain.game.model.Game.Skill.STORYTELLING)
            GameSort.VOCABULARY -> game.game.skills.contains(com.korop.yaroslavhorach.domain.game.model.Game.Skill.VOCABULARY)
            GameSort.DICTION -> game.game.skills.contains(com.korop.yaroslavhorach.domain.game.model.Game.Skill.DICTION)
            GameSort.FLIRT -> game.game.skills.contains(com.korop.yaroslavhorach.domain.game.model.Game.Skill.FLIRT)
            null -> true
        }
    }.map { game ->

        if (challenge is ChallengeExerciseMix) {
            val exerciseStatusMap = challenge.exercisesAndCompletedMark
                .associate { it.first to it.second }

            val isChallengeGame = exerciseStatusMap.containsKey(game.game.name)
            val isChallengeGameCompleted = exerciseStatusMap[game.game.name] == true

            game.copy(
                isChallengeGame = isChallengeGame,
                isChallengeGameCompleted = isChallengeGameCompleted
            )
        } else {
            game
        }
    }
}
