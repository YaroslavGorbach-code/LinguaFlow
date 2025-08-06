package com.korop.yaroslavhorach.exercises.vocabulary

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.helpers.SimpleTimer
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseName
import com.korop.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.korop.yaroslavhorach.domain.exercise_content.model.FeedBack
import com.korop.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseAction
import com.korop.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseUiMessage
import com.korop.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseViewState
import com.korop.yaroslavhorach.exercises.vocabulary.navigation.VocabularyRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VocabularyExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val gameRepository: GameRepository,
    private val exerciseContentRepository: ExerciseContentRepository
) : BaseViewModel<VocabularyExerciseViewState, VocabularyExerciseAction, VocabularyExerciseUiMessage>() {
    private val timer = SimpleTimer()

    private val exerciseId = savedStateHandle.toRoute<VocabularyRoute>().exerciseId

    private var currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    private var currentVocabulary: MutableStateFlow<Vocabulary?> = MutableStateFlow(null)

    override val pendingActions: MutableSharedFlow<VocabularyExerciseAction> = MutableSharedFlow()

    private val isExerciseStarted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val userWordsCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val wordForTask: MutableStateFlow<String> = MutableStateFlow("")

    override val state: StateFlow<VocabularyExerciseViewState> = com.korop.yaroslavhorach.common.utill.combine(
        exerciseRepository.getBlock(),
        currentVocabulary,
        isExerciseStarted,
        userWordsCount,
        wordForTask,
        uiMessageManager.message
    ) { block, currentVocabulary, isTimerStarted, wordsCount, wordForTask, message ->
        VocabularyExerciseViewState(
            exerciseBlock = block,
            wordsAmount = wordsCount,
            vocabulary = currentVocabulary,
            isExerciseActive = isTimerStarted,
            wordForTask = wordForTask,
            uiMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VocabularyExerciseViewState.Empty
    )

    init {
        viewModelScope.launch {
            currentExercise.value = exerciseRepository.getExercise(exerciseId)
            when (currentExercise.value?.name) {
                ExerciseName.CHAIN_OF_ASSOCIATIONS -> {
                    currentVocabulary.value = exerciseContentRepository.getVocabulary(Vocabulary.WordType.ASSOCIATION)
                    // raven like a chair because words are nouns there
                    wordForTask.value = exerciseContentRepository.getGameWords(Game.GameName.RAVEN_LIKE_A_CHAIR).first()
                }

                else -> {
                    currentVocabulary.value = exerciseContentRepository.getVocabulary(
                        Vocabulary.WordType.entries.getOrElse(currentExercise.value?.exerciseProgress?.progress ?: 0) {
                            Vocabulary.WordType.entries.random()
                        }
                    )
                }
            }
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    is VocabularyExerciseAction.OnNextClicked -> {
                        exerciseRepository.markCompleted(exerciseId)
                        gameRepository.requestUpdateDailyChallengeCompleteTime(listOf(Game.Skill.VOCABULARY), timer.getElapsedTimeMillis())
                        try {
                            currentExercise.value?.name?.let {
                                gameRepository.requestCompleteDailyChallengeGame(
                                    Game.GameName.valueOf(
                                        it.name
                                    )
                                )
                            }
                        } catch (_: Throwable) { }

                        uiMessageManager.emitMessage(
                            UiMessage(
                                VocabularyExerciseUiMessage.NavigateToExerciseResult(
                                    time = timer.getElapsedTimeMillis(),
                                    EXPERIENCE_REWARD,
                                    gameName = Game.GameName.valueOf(
                                        currentExercise.value?.name?.name ?: Game.GameName.VOCABULARY.name
                                    )
                                )
                            )
                        )
                    }
                    is VocabularyExerciseAction.OnStartClicked -> {
                        timer.reset()
                        timer.start()
                        userWordsCount.value = 0

                        isExerciseStarted.value = true
                    }
                    is VocabularyExerciseAction.OnTryAgainClicked -> {
                        timer.reset()
                        userWordsCount.value = 0
                    }
                    is VocabularyExerciseAction.OnTimerFinished -> {
                        val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

                        timer.stop()
                        isExerciseStarted.value = false

                        when (val feedback = currentVocabulary.value!!.getFeedback(userWordsCount.value, lang)) {
                            is FeedBack.Bad -> {
                                uiMessageManager.emitMessage(
                                    UiMessage(
                                        VocabularyExerciseUiMessage.ShowBadResult(
                                            amountWords = userWordsCount.value,
                                            text = feedback.text
                                        )
                                    )
                                )
                            }
                            is FeedBack.Good -> {
                                uiMessageManager.emitMessage(
                                    UiMessage(
                                        VocabularyExerciseUiMessage.ShowCorrectResult(
                                            amountWords = userWordsCount.value,
                                            text = feedback.text
                                        )
                                    )
                                )
                            }
                            is FeedBack.NotBad -> {
                                uiMessageManager.emitMessage(
                                    UiMessage(
                                        VocabularyExerciseUiMessage.ShowNotBadResult(
                                            amountWords = userWordsCount.value,
                                            text = feedback.text
                                        )
                                    )
                                )
                            }
                        }
                    }
                    is VocabularyExerciseAction.OnScreenClicked -> {
                        if (state.value.isExerciseActive) {
                            userWordsCount.value = userWordsCount.value.inc()
                        }
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }

    companion object {
        const val EXPERIENCE_REWARD = 25
    }
}
