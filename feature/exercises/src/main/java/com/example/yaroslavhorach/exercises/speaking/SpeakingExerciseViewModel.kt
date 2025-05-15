package com.example.yaroslavhorach.exercises.speaking

import android.Manifest
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.helpers.AudioRecorder
import com.example.yaroslavhorach.common.helpers.PermissionManager
import com.example.yaroslavhorach.common.helpers.SimpleTimer
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.common.utill.combine
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.exercises.R
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseAction
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseUiMessage
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseViewState
import com.example.yaroslavhorach.exercises.speaking.navigation.SpeakingExerciseRoute
import com.example.yaroslavhorach.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class SpeakingExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val permissionManager: PermissionManager,
    private val recorder: AudioRecorder,
    private val exerciseRepository: ExerciseRepository,
    private val exerciseContentRepository: ExerciseContentRepository,
   @ApplicationContext private val applicationContext: Context
) : BaseViewModel<SpeakingExerciseViewState, SpeakingExerciseAction, SpeakingExerciseUiMessage>() {
    private val timer = SimpleTimer()

    private val exerciseId = savedStateHandle.toRoute<SpeakingExerciseRoute>().exerciseId

    private var currentExercise: Exercise? = null

    override val pendingActions: MutableSharedFlow<SpeakingExerciseAction> = MutableSharedFlow()

    private val mode: MutableStateFlow<SpeakingExerciseViewState.ScreenMode?> = MutableStateFlow(null)

    private val btnTooltipText: MutableStateFlow<UiText> = MutableStateFlow(UiText.Empty)
    private val isSpeakingResulVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val overAllMaxProgress: MutableStateFlow<Int> = MutableStateFlow(1)
    private val overAllProgress: MutableStateFlow<Int> = MutableStateFlow(0)
    private val overAllProgressValue = combine(overAllMaxProgress, overAllProgress) { overAllMaxProgress, overAllProgress ->
            (overAllProgress.toFloat() / overAllMaxProgress.toFloat())
        }

    private var tests: Queue<Test> = ArrayDeque()

    override val state: StateFlow<SpeakingExerciseViewState> = combine(
        btnTooltipText,
        recorder.isRecordingFlow,
        recorder.isRationalToAllowStopManually,
        recorder.isSpeakingFlow,
        recorder.amplitudeFlow,
        recorder.secondsLeftFlow,
        recorder.playProgressFlow,
        recorder.isPlayingPausedFlow,
        mode,
        overAllProgressValue,
        isSpeakingResulVisible,
        uiMessageManager.message
    ) { btnTooltipText, isRecording, isRationalToAllowStopManually, isSpeaking, amplitude, secondsTillFinish, playProgress, isPlayingRecordPaused, mode, progress, isSpeakingResulVisible, messages ->
        SpeakingExerciseViewState(
            mode = when (mode) {
                is SpeakingExerciseViewState.ScreenMode.IntroTest -> mode
                is SpeakingExerciseViewState.ScreenMode.Speaking -> mode.copy(
                    isRecording = isRecording,
                    isSpeaking = isSpeaking,
                    amplitude = amplitude,
                    secondsTillFinish = secondsTillFinish,
                    isStopRecordingBtnVisible = isRationalToAllowStopManually,
                    result = mode.result.copy(
                        isVisible = isSpeakingResulVisible,
                        playProgress = playProgress,
                        isPlayingRecordPaused = isPlayingRecordPaused
                    )
                )
                null -> null
            },
            btnTooltipText = btnTooltipText,
            progress = progress,
            uiMessage = messages,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SpeakingExerciseViewState.Empty
    )

    init {
        setUpInitialMode()

        recorder.onStopRecordingFlow
            .onEach {
                overAllProgress.value = overAllProgress.value.inc()
                isSpeakingResulVisible.value = true
            }
            .launchIn(viewModelScope)

        pendingActions
            .onEach { event ->
                when (event) {
                    is SpeakingExerciseAction.OnStartSpikingClicked -> {
                        btnTooltipText.value = UiText.Empty
                        startAudioRecording()
                    }
                    is SpeakingExerciseAction.OnCheckTestVariantClicked -> {
                        checkTestAnswer()
                    }
                    is SpeakingExerciseAction.OnVariantChosen -> {
                        choseTestVariant(event)
                    }
                    is SpeakingExerciseAction.OnNextTestClicked -> {
                        handleNextTest()
                    }
                    is SpeakingExerciseAction.OnNextSituationClicked -> {
                        isSpeakingResulVisible.value = false
                        recorder.pausePlayback()

                        handleNextSituation()
                    }
                    is SpeakingExerciseAction.OnTryAgainSituationClicked -> {
                        isSpeakingResulVisible.value = false
                        recorder.pausePlayback()
                    }
                    is SpeakingExerciseAction.OnPlayRecordClicked -> {
                        recorder.playLastRecording()
                    }
                    is SpeakingExerciseAction.OnPauseRecordClicked -> {
                        recorder.pausePlayback()
                    }
                    is SpeakingExerciseAction.OnStopSpeakingClicked -> {
                       viewModelScope.launch { recorder.stopRecording() }
                    }
                    else -> error("Action $event is not handled")
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun handleNextSituation() {
        if (overAllProgress.value == overAllMaxProgress.value) {
            timer.stop()
            exerciseRepository.markCompleted(exerciseId)

            uiMessageManager.emitMessage(
                UiMessage(
                    SpeakingExerciseUiMessage.NavigateToExerciseResult(
                        time = timer.getElapsedTimeMillis(),
                        experience = EXPERIENCE_REWARD
                    )
                )
            )
        } else {
            val situation = exerciseContentRepository.getSituation(currentExercise!!.name)
            (mode.value as? SpeakingExerciseViewState.ScreenMode.Speaking)?.let { mode ->
                this.mode.value = mode.copy(situation = situation)
            }
        }
    }

    private fun handleNextTest() {
        tests.poll()?.let { test ->
            when (val mode = mode.value) {
                is SpeakingExerciseViewState.ScreenMode.IntroTest -> {
                    this.mode.value = mode.copy(test)
                }

                else -> {}
            }
        } ?: run {
            setUpSpeakingModeAfterTests(exerciseContentRepository)
        }
    }

    private fun choseTestVariant(event: SpeakingExerciseAction.OnVariantChosen) {
        when (val mode = mode.value) {
            is SpeakingExerciseViewState.ScreenMode.IntroTest -> {
                this.mode.value = mode.copy(chosenVariant = event.variant)
            }

            else -> {}
        }
    }

    private suspend fun checkTestAnswer() {
        when (val mode = mode.value) {
            is SpeakingExerciseViewState.ScreenMode.IntroTest -> {
                if (mode.chosenVariant?.isCorrect == true) {
                    uiMessageManager.emitMessage(
                        UiMessage(
                            SpeakingExerciseUiMessage.ShowCorrectAnswerExplanation(
                                mode.test.correctAnswerExplanation
                            )
                        )
                    )
                } else {
                    uiMessageManager.emitMessage(
                        UiMessage(
                            SpeakingExerciseUiMessage.ShowWrongAnswerExplanation(
                                mode.test.wrongAnswerExplanation
                            )
                        )
                    )
                }

                val progress = if (mode.chosenVariant?.isCorrect == true) mode.progress.inc() else mode.progress
                overAllProgress.value = progress

                this.mode.value = mode.copy(chosenVariant = null, progress = progress)
            }

            else -> {}
        }
    }

    private suspend fun startAudioRecording() {
        if (permissionManager.checkPermission(Manifest.permission.RECORD_AUDIO)) {
            recorder.startRecording(applicationContext)
        } else {
            uiMessageManager.emitMessage(UiMessage(SpeakingExerciseUiMessage.RequestRecordAudio))
        }
    }

    private fun setUpInitialMode() {
        timer.start()

        viewModelScope.launch {
            currentExercise = exerciseRepository.getExercise(exerciseId)

            mode.value = if (currentExercise!!.exerciseProgress.progress == 0) {
                setUpTestsModeIfPossible()
            } else {
                setUpSpeakingMode()
            }
        }
    }

    private fun setUpSpeakingModeAfterTests(exerciseContentRepository: ExerciseContentRepository) = viewModelScope.launch {
            btnTooltipText.value = UiText.RandomFromResourceArray(R.array.speaking_exercise_intro_text)

            val situation = exerciseContentRepository.getSituation(currentExercise!!.name)

            mode.value = SpeakingExerciseViewState.ScreenMode.Speaking(situation, maxProgress = AFTER_TEST_SPEAKING_MAX_PROGRESS)
        }

    private suspend fun setUpSpeakingMode(): SpeakingExerciseViewState.ScreenMode.Speaking {
        overAllMaxProgress.value = SPEAKING_MAX_PROGRESS
        btnTooltipText.value = UiText.RandomFromResourceArray(R.array.speaking_exercise_motivation)

        return SpeakingExerciseViewState.ScreenMode.Speaking(
            exerciseContentRepository.getSituation(currentExercise!!.name),
            maxProgress = SPEAKING_MAX_PROGRESS
        )
    }

    private suspend fun setUpTestsModeIfPossible(): SpeakingExerciseViewState.ScreenMode {
        tests = ArrayDeque(exerciseContentRepository.getTests(currentExercise!!.name))

        return if (tests.isEmpty().not()) {
            overAllMaxProgress.value = tests.size + AFTER_TEST_SPEAKING_MAX_PROGRESS

            SpeakingExerciseViewState.ScreenMode.IntroTest(tests.poll()!!, 0, tests.size)
        } else {
            overAllMaxProgress.value = SPEAKING_MAX_PROGRESS
            btnTooltipText.value = UiText.RandomFromResourceArray(R.array.speaking_exercise_motivation)

            SpeakingExerciseViewState.ScreenMode.Speaking(
                exerciseContentRepository.getSituation(
                    currentExercise!!.name
                ),
                maxProgress = SPEAKING_MAX_PROGRESS
            )
        }
    }

    companion object {
        const val EXPERIENCE_REWARD = 10
        const val AFTER_TEST_SPEAKING_MAX_PROGRESS = 2
        const val SPEAKING_MAX_PROGRESS = 3
    }
}
