package com.example.yaroslavhorach.exercises.speaking

import android.Manifest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.yaroslavhorach.common.base.BaseViewModel
import com.example.yaroslavhorach.common.helpers.AudioRecorder
import com.example.yaroslavhorach.common.helpers.PermissionManager
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.common.utill.combine
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseAction
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseUiMessage
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseViewState
import com.example.yaroslavhorach.exercises.speaking.navigation.SpeakingExerciseRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    exerciseRepository: ExerciseRepository,
    exerciseContentRepository: ExerciseContentRepository
) : BaseViewModel<SpeakingExerciseViewState, SpeakingExerciseAction, SpeakingExerciseUiMessage>() {

    private val exerciseId = savedStateHandle.toRoute<SpeakingExerciseRoute>().exerciseId

    private var currentExercise: Exercise? = null

    override val pendingActions: MutableSharedFlow<SpeakingExerciseAction> = MutableSharedFlow()

    private val mode: MutableStateFlow<SpeakingExerciseViewState.ScreenMode?> = MutableStateFlow(null)

    private var tests: Queue<Test> = ArrayDeque()

    override val state: StateFlow<SpeakingExerciseViewState> = combine(
            recorder.isRecordingFlow,
            recorder.isSpeakingFlow,
            recorder.amplitudeFlow,
            recorder.secondsLeftFlow,
            mode,
            uiMessageManager.message
        ) { isRecording, isSpeaking, amplitude, secondsTillFinish, mode, messages ->
            SpeakingExerciseViewState(
                mode = when(mode){
                    is SpeakingExerciseViewState.ScreenMode.IntroTest -> mode
                    is SpeakingExerciseViewState.ScreenMode.Speaking -> mode.copy(
                        isRecording = isRecording,
                        isSpeaking = isSpeaking,
                        amplitude = amplitude,
                        secondsTillFinish = secondsTillFinish,
                    )
                    null -> null
                },
                uiMessage = messages,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SpeakingExerciseViewState.Empty
        )

    init {
        setUpInitialMode(exerciseRepository, exerciseContentRepository)

        pendingActions
            .onEach { event ->
                when (event) {
                    is SpeakingExerciseAction.OnStartSpikingClicked -> {
                        startAudioRecording()
                    }
                    is SpeakingExerciseAction.OnCheckTestVariantClicked -> {
                        checkTestAnswer()
                    }
                    is SpeakingExerciseAction.OnVariantChosen -> {
                        choseTestVariant(event)
                    }
                    is SpeakingExerciseAction.OnNextTestClicked -> {
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
                }
            }
            .launchIn(viewModelScope)
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

                this.mode.value = mode.copy(chosenVariant = null, progress = progress)
            }

            else -> {}
        }
    }

    private suspend fun startAudioRecording() {
        if (permissionManager.checkPermission(Manifest.permission.RECORD_AUDIO)) {
            recorder.start()
        } else {
            uiMessageManager.emitMessage(UiMessage(SpeakingExerciseUiMessage.RequestRecordAudio))
        }
    }

    private fun setUpSpeakingModeAfterTests(exerciseContentRepository: ExerciseContentRepository) =
        viewModelScope.launch {
            val situation = exerciseContentRepository.getSituation(currentExercise!!.exerciseName)

            mode.value = SpeakingExerciseViewState.ScreenMode.Speaking(situation, maxProgress = 2)
        }

    private fun setUpInitialMode(
        exerciseRepository: ExerciseRepository,
        exerciseContentRepository: ExerciseContentRepository
    ) {
        viewModelScope.launch {
            currentExercise = exerciseRepository.getExercise(exerciseId)

            mode.value = if (currentExercise!!.exerciseProgress.progress == 0) {
                tests = ArrayDeque(exerciseContentRepository.getTests(currentExercise!!.exerciseName))

                if (tests.isEmpty().not()) {
                    SpeakingExerciseViewState.ScreenMode.IntroTest(tests.poll()!!, 0, tests.size + 1)
                } else {
                    SpeakingExerciseViewState.ScreenMode.Speaking(
                        exerciseContentRepository.getSituation(
                            currentExercise!!.exerciseName
                        )
                    )
                }
            } else {
                SpeakingExerciseViewState.ScreenMode.Speaking(exerciseContentRepository.getSituation(currentExercise!!.exerciseName))
            }
        }
    }
}
