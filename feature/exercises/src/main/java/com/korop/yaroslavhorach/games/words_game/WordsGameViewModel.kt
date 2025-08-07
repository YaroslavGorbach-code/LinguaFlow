package com.korop.yaroslavhorach.games.words_game

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.helpers.SimpleTimer
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import com.korop.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.exercises.speaking.SpeakingExerciseViewModel.Companion.EXPERIENCE_REWARD
import com.korop.yaroslavhorach.games.words_game.model.WordsGameAction
import com.korop.yaroslavhorach.games.words_game.model.WordsGameUiMessage
import com.korop.yaroslavhorach.games.words_game.model.WordsGameViewState
import com.korop.yaroslavhorach.games.words_game.navigation.WordsGameNavigationRoute
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
import kotlin.math.roundToInt

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

    override val state: StateFlow<WordsGameViewState> = com.korop.yaroslavhorach.common.utill.combine(
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
                Game.GameName.ANTONYM_BATTLE,
                Game.GameName.RHYME_LIGHTNING,
                Game.GameName.ONE_LETTER,
                Game.GameName.FIVE_TO_THE_POINT,
                Game.GameName.VOCABULARY_BURST,
                Game.GameName.QUICK_ASSOCIATION,
                Game.GameName.WORST_IN_THE_WORLD,
                Game.GameName.WORD_BY_CATEGORY,
                Game.GameName.WORD_CANNON,
                Game.GameName.EMOJI_STORY,
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
                Game.GameName.BREATHLINE_CHALLENGE,
                Game.GameName.UNUSUAL_PROBLEM_SOLVER,
                Game.GameName.CONSONANT_BATTLE,
                Game.GameName.EMOTIONAL_TRANSLATOR -> {
                    game.value?.let(::getGameSentence)
                }
                else -> TODO()
            }
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    is WordsGameAction.OnNextClicked -> {
                        if (state.value.isLastExercise) {
                            gameRepository.requestUpdateDailyChallengeCompleteTime(
                                game.value?.skills ?: emptyList(),
                                timer.getElapsedTimeMillis()
                            )
                            game.value?.name?.let { gameRepository.requestCompleteDailyChallengeGame(it) }

                            val elapsedTime = timer.getElapsedTimeMillis()
                            val requiredTime = 2 * 60 * 1000L

                            val experience = if (elapsedTime >= requiredTime) {
                                EXPERIENCE_REWARD
                            } else {
                                val proportional = (EXPERIENCE_REWARD * elapsedTime.toDouble() / requiredTime)
                                proportional.roundToInt()
                            }

                            uiMessageManager.emitMessage(
                                UiMessage(
                                    WordsGameUiMessage.NavigateToExerciseResult(
                                        time = timer.getElapsedTimeMillis(),
                                        experience = experience
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
}
