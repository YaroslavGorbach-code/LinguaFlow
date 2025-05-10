package com.example.yaroslavhorach.exercises.speaking.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test

data class SpeakingExerciseViewState(
    val mode: ScreenMode? = null,
    val uiMessage: UiMessage<SpeakingExerciseUiMessage>? = null
) {

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
