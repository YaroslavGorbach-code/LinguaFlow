package com.example.yaroslavhorach.exercises.vocabulary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.helpers.SimpleTimer
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.exercise_content.model.FeedBack
import com.example.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.exercises.speaking.navigation.SpeakingExerciseRoute
import com.example.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseAction
import com.example.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseUiMessage
import com.example.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseViewState
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
class VocabularyExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val gameRepository: GameRepository,
    private val exerciseContentRepository: ExerciseContentRepository
) : BaseViewModel<VocabularyExerciseViewState, VocabularyExerciseAction, VocabularyExerciseUiMessage>() {
    private val timer = SimpleTimer()

    private val exerciseId = savedStateHandle.toRoute<SpeakingExerciseRoute>().exerciseId

    private var currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    private var currentVocabulary: MutableStateFlow<Vocabulary?> = MutableStateFlow(null)

    override val pendingActions: MutableSharedFlow<VocabularyExerciseAction> = MutableSharedFlow()

    private val isExerciseStarted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val userWordsCount: MutableStateFlow<Int> = MutableStateFlow(0)

    override val state: StateFlow<VocabularyExerciseViewState> = com.example.yaroslavhorach.common.utill.combine(
        exerciseRepository.getBlock(),
        currentVocabulary,
        isExerciseStarted,
        userWordsCount,
        uiMessageManager.message
    ) { block, currentVocabulary, isTimerStarted, wordsCount, message ->
        VocabularyExerciseViewState(
            exerciseBlock = block,
            wordsAmount = wordsCount,
            vocabulary = currentVocabulary,
            isExerciseActive = isTimerStarted,
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
            currentVocabulary.value = exerciseContentRepository.getVocabulary(
                Vocabulary.WordType.entries.getOrElse(currentExercise.value?.exerciseProgress?.progress ?: 0) {
                    Vocabulary.WordType.entries.random()
                }
            )
        }

        pendingActions
            .onEach { event ->
                when (event) {
                    is VocabularyExerciseAction.OnNextClicked -> {
                        exerciseRepository.markCompleted(exerciseId)
                        gameRepository.requestUpdateDailyChallengeCompleteTime(listOf(Game.Skill.VOCABULARY), timer.getElapsedTimeMillis())
                        uiMessageManager.emitMessage(
                            UiMessage(
                                VocabularyExerciseUiMessage.NavigateToExerciseResult(
                                    timer.getElapsedTimeMillis(),
                                    EXPERIENCE_REWARD
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
                        timer.stop()
                        isExerciseStarted.value = false

                        when (val feedback = currentVocabulary.value!!.getFeedback(userWordsCount.value)) {
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
