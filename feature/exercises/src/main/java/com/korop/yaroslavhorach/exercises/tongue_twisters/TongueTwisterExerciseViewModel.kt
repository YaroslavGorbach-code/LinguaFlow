package com.korop.yaroslavhorach.exercises.tongue_twisters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.korop.yaroslavhorach.common.base.BaseViewModel
import com.korop.yaroslavhorach.common.helpers.SimpleTimer
import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.mapToTongueTwistDifficulty
import com.korop.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.korop.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.exercises.tongue_twisters.model.TongueTwisterExerciseAction
import com.korop.yaroslavhorach.exercises.tongue_twisters.model.TongueTwisterExerciseUiMessage
import com.korop.yaroslavhorach.exercises.tongue_twisters.model.TongueTwisterExerciseViewState
import com.korop.yaroslavhorach.exercises.tongue_twisters.navigation.TongueTwistersExerciseRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class TongueTwisterExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val gameRepository: GameRepository,
    private val exerciseContentRepository: ExerciseContentRepository
) : BaseViewModel<TongueTwisterExerciseViewState, TongueTwisterExerciseAction, TongueTwisterExerciseUiMessage>() {
    private val timer = SimpleTimer()

    private val exerciseId = savedStateHandle.toRoute<TongueTwistersExerciseRoute>().exerciseId

    override val pendingActions: MutableSharedFlow<TongueTwisterExerciseAction> = MutableSharedFlow()

    private var currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    private val twistSpeakingMod: MutableStateFlow<TongueTwisterExerciseViewState.TwistSpeakingMod> = MutableStateFlow(
        TongueTwisterExerciseViewState.TwistSpeakingMod.SLOW
    )
    private val currentTwist: MutableStateFlow<TongueTwister?> = MutableStateFlow(null)
    private val overAllMaxProgress: MutableStateFlow<Int> = MutableStateFlow(
        TongueTwister.Difficulty.entries.size * MAX_PROGRESS
    )
    private val overAllProgress: MutableStateFlow<Int> = MutableStateFlow(1)
    private val overAllProgressValue =
        combine(overAllMaxProgress, overAllProgress) { overAllMaxProgress, overAllProgress ->
            (overAllProgress.toFloat() / overAllMaxProgress.toFloat())
        }

    override val state: StateFlow<TongueTwisterExerciseViewState> = combine(
        twistSpeakingMod,
        overAllProgressValue,
        currentTwist,
        exerciseRepository.getBlock(),
        uiMessageManager.message
    ) { speakingMod, progress, twist, block, message ->
        TongueTwisterExerciseViewState(
            progress = progress,
            twistSpeakingMod = speakingMod,
            block = block,
            twist = twist,
            uiMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TongueTwisterExerciseViewState.Empty
    )

    init {
        setUpInitialTwist()

        pendingActions
            .onEach { event ->
                when (event) {
                    is TongueTwisterExerciseAction.OnNextClicked -> {
                        handleOnNextClicked()
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleOnNextClicked() {
        when (twistSpeakingMod.value) {
            TongueTwisterExerciseViewState.TwistSpeakingMod.SLOW -> {
                overAllProgress.value = overAllProgress.value.inc()
                twistSpeakingMod.value = TongueTwisterExerciseViewState.TwistSpeakingMod.MEDIUM
            }
            TongueTwisterExerciseViewState.TwistSpeakingMod.MEDIUM -> {
                overAllProgress.value = overAllProgress.value.inc()
                twistSpeakingMod.value = TongueTwisterExerciseViewState.TwistSpeakingMod.FAST
            }
            TongueTwisterExerciseViewState.TwistSpeakingMod.FAST -> {
                if (overAllProgress.value == overAllMaxProgress.value) {
                    timer.stop()

                    viewModelScope.launch {
                        exerciseRepository.markCompleted(exerciseId)
                        gameRepository.requestUpdateDailyChallengeCompleteTime(listOf(Game.Skill.DICTION), timer.getElapsedTimeMillis())
                        try {
                            currentExercise.value?.name?.let {
                                gameRepository.requestCompleteDailyChallengeGame(
                                    Game.GameName.valueOf(
                                        it.name
                                    )
                                )
                            }
                        } catch (_: Throwable) { }

                        val elapsedTime = timer.getElapsedTimeMillis()
                        val requiredTime = 2 * 60 * 1000L

                        val experience = if (elapsedTime >= requiredTime) {
                            EXPERIENCE_REWORD
                        } else {
                            val proportional = (EXPERIENCE_REWORD * elapsedTime.toDouble() / requiredTime)
                            proportional.roundToInt()
                        }

                        uiMessageManager.emitMessage(
                            UiMessage(
                                TongueTwisterExerciseUiMessage.NavigateToExerciseResult(
                                    time = timer.getElapsedTimeMillis(),
                                    experience
                                )
                            )
                        )
                    }
                } else {
                    overAllProgress.value = overAllProgress.value.inc()

                    viewModelScope.launch {
                        currentTwist.value = currentExercise.value?.name?.mapToTongueTwistDifficulty()?.let {
                            exerciseContentRepository.getTongueTwister(it)
                        }

                        twistSpeakingMod.value = TongueTwisterExerciseViewState.TwistSpeakingMod.SLOW
                    }
                }

            }
        }
    }

    private fun setUpInitialTwist() {
        timer.start()

        viewModelScope.launch {
            currentExercise.value = exerciseRepository.getExercise(exerciseId)

            currentTwist.value = currentExercise.value?.name?.mapToTongueTwistDifficulty()?.let {
                exerciseContentRepository.getTongueTwister(it)
            }
        }
    }

    companion object {
       private const val MAX_PROGRESS = 3
       private const val EXPERIENCE_REWORD = 25
    }
}
