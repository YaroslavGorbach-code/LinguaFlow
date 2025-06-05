package com.example.yaroslavhorach.games.words_game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.helpers.SimpleTimer
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.games.words_game.model.WordsGameAction
import com.example.yaroslavhorach.games.words_game.model.WordsGameUiMessage
import com.example.yaroslavhorach.games.words_game.model.WordsGameViewState
import com.example.yaroslavhorach.games.words_game.navigation.WordsGameNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository,
    exerciseRepository: ExerciseRepository,
    private val exerciseContentRepository: ExerciseContentRepository,
) : BaseViewModel<WordsGameViewState, WordsGameAction, WordsGameUiMessage>() {

    private val timer = SimpleTimer()

    private val gameId = savedStateHandle.toRoute<WordsGameNavigationRoute>().gameId

    override val pendingActions: MutableSharedFlow<WordsGameAction> = MutableSharedFlow()

    private val game: MutableStateFlow<Game?> = MutableStateFlow(null)


    private val words: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private val wordsUsed: MutableStateFlow<Int> = MutableStateFlow(0)

    override val state: StateFlow<WordsGameViewState> = com.example.yaroslavhorach.common.utill.combine(
        game,
        words,
        wordsUsed,
        exerciseRepository.getBlock(),
        uiMessageManager.message
    ) { game, words, wordsUsed, block, message ->
        WordsGameViewState(
            progress = wordsUsed.toFloat() / (game?.maxProgress?.toFloat() ?: 1f),
            game = game,
            isLastExercise = game?.maxProgress == wordsUsed,
            words = words,
            block = block,
            uiMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WordsGameViewState.Empty
    )

    init {
        timer.start()

        viewModelScope.launch {
            game.value = gameRepository.getGame(gameId)
            game.value?.let(::getGameWords)
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    is WordsGameAction.OnNextClicked -> {
                        if (state.value.isLastExercise){
                            uiMessageManager.emitMessage(
                                UiMessage(
                                    WordsGameUiMessage.NavigateToExerciseResult(
                                        timer.getElapsedTimeMillis(),
                                        EXPERIENCE_REWORD
                                    )
                                )
                            )
                        } else {
                            game.value?.let(::getGameWords)
                        }
                    }

                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getGameWords(game: Game) {
        viewModelScope.launch {
            wordsUsed.value = wordsUsed.value.inc()
            words.value = exerciseContentRepository.getGameWords(game.name)
        }
    }

    companion object {
        private const val EXPERIENCE_REWORD = 10
    }
}
