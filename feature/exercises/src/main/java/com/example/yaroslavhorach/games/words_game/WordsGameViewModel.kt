package com.example.yaroslavhorach.games.words_game

import android.util.Log
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

    private val screenMode: MutableStateFlow<WordsGameViewState.ScreenMode> = MutableStateFlow(WordsGameViewState.ScreenMode.Words(
        emptyList()
    ))

    private val progress: MutableStateFlow<Int> = MutableStateFlow(0)

    override val state: StateFlow<WordsGameViewState> = com.example.yaroslavhorach.common.utill.combine(
        game,
        progress,
        exerciseRepository.getBlock(),
        screenMode,
        uiMessageManager.message
    ) { game, progress, block, screenMode, message ->
        WordsGameViewState(
            progress = progress.toFloat() / (game?.maxProgress?.toFloat() ?: 1f),
            game = game,
            isLastExercise = game?.maxProgress == progress,
            block = block,
            uiMessage = message,
            screenMode = screenMode
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
            when (game.value?.name) {
                Game.GameName.RAVEN_LIKE_A_CHAIR,
                Game.GameName.FOUR_WORDS_ONE_STORY,
                Game.GameName.TALK_TILL_EXHAUSTED,
                Game.GameName.SELL_THIS_THING,
                Game.GameName.RAP_IMPROV,
                Game.GameName.ONE_WORD_MANY_MEANINGS,
                Game.GameName.FLIRTING_WITH_OBJECT,
                Game.GameName.BOTH_THERE_AND_IN_BED,
                Game.GameName.HOT_WORD,
                Game.GameName.DEFINE_PRECISELY -> {
                    game.value?.let(::getGameWords)
                }
                Game.GameName.BIG_ANSWER,
                Game.GameName.DEVILS_ADVOCATE,
                Game.GameName.DIALOGUE_WITH_SELF,
                Game.GameName.IMAGINARY_SITUATION,
                Game.GameName.EMOTION_TO_FACT,
                Game.GameName.WHO_AM_I_MONOLOGUE,
                Game.GameName.I_AM_EXPERT,
                Game.GameName.FORBIDDEN_WORDS,
                Game.GameName.BODY_LANGUAGE_EXPRESS,
                Game.GameName.PERSUASIVE_SHOUT,
                Game.GameName.SUBTLE_MANIPULATION,
                Game.GameName.ONE_SYNONYM_PLEASE,
                Game.GameName.INTONATION_MASTER,
                Game.GameName.FUNNIEST_ANSWER,
                Game.GameName.MADMAN_ANNOUNCEMENT,
                Game.GameName.FUNNY_EXCUSE,
                Game.GameName.EMOTIONAL_TRANSLATOR -> {
                    game.value?.let(::getGameSentence)
                }
                Game.GameName.WORD_IN_TEMPO -> TODO()
                Game.GameName.ANTONYM_BATTLE -> TODO()
                Game.GameName.RHYME_LIGHTNING -> TODO()
                Game.GameName.DOUBLE_MEANING_WORDS -> TODO()
                null -> TODO()
            }
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
                            when (state.value.screenMode) {
                                is WordsGameViewState.ScreenMode.Sentence -> game.value?.let(::getGameSentence)
                                is WordsGameViewState.ScreenMode.Words -> game.value?.let(::getGameWords)
                            }
                        }
                    }

                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getGameWords(game: Game) {
        viewModelScope.launch {
            progress.value = progress.value.inc()
            screenMode.value = WordsGameViewState.ScreenMode.Words(exerciseContentRepository.getGameWords(game.name))
        }
    }

    private fun getGameSentence(game: Game) {
        viewModelScope.launch {
            progress.value = progress.value.inc()
            screenMode.value = WordsGameViewState.ScreenMode.Sentence(exerciseContentRepository.getGameSentence(game.name))
        }
    }

    companion object {
        private const val EXPERIENCE_REWORD = 10
    }
}
