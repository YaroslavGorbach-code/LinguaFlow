package com.example.yaroslavhorach.exercises.speaking.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test

data class SpeakingExerciseViewState(
    val exerciseBlock: ExerciseBlock = ExerciseBlock.ONE,
    val mode: ScreenMode? = null,
    val progress: Float = 0f,
    val uiMessage: UiMessage<SpeakingExerciseUiMessage>? = null
) {
    val topBarBgRes: Int
        get() = when (exerciseBlock) {
            ExerciseBlock.ONE -> com.example.yaroslavhorach.designsystem.R.drawable.block_1_gragient
            ExerciseBlock.TWO -> com.example.yaroslavhorach.designsystem.R.drawable.block_1_gragient
            ExerciseBlock.THREE -> com.example.yaroslavhorach.designsystem.R.drawable.block_1_gragient
            ExerciseBlock.FOUR -> com.example.yaroslavhorach.designsystem.R.drawable.block_1_gragient
            ExerciseBlock.FIVE -> com.example.yaroslavhorach.designsystem.R.drawable.block_1_gragient
            ExerciseBlock.SIX -> com.example.yaroslavhorach.designsystem.R.drawable.block_1_gragient
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
        ) : ScreenMode() {
            companion object {
                const val MAX_PROGRESS = 3
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
