package com.example.yaroslavhorach.exercises.speaking.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.ui.UiText

data class SpeakingExerciseViewState(
    val btnTooltipText: UiText = UiText.Empty,
    val exerciseBlock: ExerciseBlock = ExerciseBlock.ONE,
    val mode: ScreenMode? = null,
    val progress: Float = 0f,
    val uiMessage: UiMessage<SpeakingExerciseUiMessage>? = null
) {
    val topBarBgRes: Int
        get() = when (exerciseBlock) {
            ExerciseBlock.ONE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
            ExerciseBlock.TWO -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
            ExerciseBlock.THREE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
            ExerciseBlock.FOUR -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
            ExerciseBlock.FIVE -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
            ExerciseBlock.SIX -> com.example.yaroslavhorach.designsystem.R.drawable.gradient_orange
        }

    sealed class ScreenMode {
        data class IntroTest(
            val test: Test,
            val progress: Int,
            val maxProgress: Int,
            val chosenVariant: Test.Variant? = null
        ) : ScreenMode()

        data class Speaking(
            val situation: Situation,
            val progress: Int = 0,
            val maxProgress: Int? = null,
            val amplitude: Int? = null,
            val isRecording: Boolean = false,
            val isSpeaking: Boolean = false,
            val secondsTillFinish: Int = 0,
            val isStopRecordingBtnVisible: Boolean = false,
            val result: Result = Result()
        ) : ScreenMode() {

            data class Result(
                val isVisible: Boolean = false,
                val playProgress: Float = 0f,
                val isPlayingRecordPaused: Boolean = false,
            ) {
                val isPlaying
                    get() = playProgress > 0f && playProgress < 1f
            }
        }
    }

    companion object {
        val Empty = SpeakingExerciseViewState()
        val PreviewSpeaking = SpeakingExerciseViewState(
            mode = ScreenMode.Speaking(
                situation = Situation(
                    1,
                    ExerciseName.ICEBREAKERS,
                    mapOf("ru" to "Ти вперше бачиш нового сусіда по сходовому майданчику."),
                    mapOf("ru" to "Привітайся та почни розмову")
                )
            )
        )
        val PreviewTest = SpeakingExerciseViewState(
            mode = ScreenMode.IntroTest(
                progress = 0,
                maxProgress = 0,
                test = Test(
                    1,
                    ExerciseName.ICEBREAKERS,
                    situation = mapOf("ru" to "Ти вперше бачиш нового сусіда по сходовому майданчику."),
                    task = mapOf("ru" to "Привітайся та почни розмову"),
                    correctAnswer = mapOf("ru" to "Привітайся та почни розмову"),
                    wrongAnswer = mapOf("ru" to "Привітайся та почни розмову"),
                    variants = listOf(
                        Test.Variant(mapOf("ru" to "Привітайся та почни розмову"), false),
                        Test.Variant(mapOf("ru" to "Віка ПРивіт!"), true),
                        Test.Variant(mapOf("ru" to "Привітайся"), false)
                    )
                )
            )
        )
    }
}
