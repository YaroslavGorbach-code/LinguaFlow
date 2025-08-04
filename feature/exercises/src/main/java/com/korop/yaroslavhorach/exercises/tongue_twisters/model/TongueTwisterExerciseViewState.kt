package com.korop.yaroslavhorach.exercises.tongue_twisters.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseName
import com.korop.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.korop.yaroslavhorach.exercises.R
import com.korop.yaroslavhorach.ui.UiText

data class TongueTwisterExerciseViewState(
    val twist: TongueTwister? = null,
    val block: ExerciseBlock = ExerciseBlock.ONE,
    val progress: Float = 0f,
    val twistSpeakingMod: TwistSpeakingMod = TwistSpeakingMod.SLOW,
    val exerciseName: ExerciseName = ExerciseName.TONGUE_TWISTERS_EASY,
    val uiMessage: UiMessage<TongueTwisterExerciseUiMessage>? = null
) {
    val title: UiText
        get() = when (twistSpeakingMod) {
            TwistSpeakingMod.SLOW -> UiText.FromResource(R.string.tongue_twister_slow_speaking_mode_title)
            TwistSpeakingMod.MEDIUM -> UiText.FromResource(R.string.tongue_twister_medium_speaking_mode_title)
            TwistSpeakingMod.FAST -> UiText.FromResource(R.string.tongue_twister_fast_speaking_mode_title)
        }

    val description: UiText
        get() = when (twistSpeakingMod) {
            TwistSpeakingMod.SLOW -> UiText.FromResource(R.string.tongue_twister_slow_speaking_mode_description)
            TwistSpeakingMod.MEDIUM -> UiText.FromResource(R.string.tongue_twister_medium_speaking_mode_description)
            TwistSpeakingMod.FAST -> UiText.FromResource(R.string.tongue_twister_fast_speaking_mode_description)
        }

    enum class TwistSpeakingMod {
        SLOW, MEDIUM, FAST
    }

    companion object {
        val Empty = TongueTwisterExerciseViewState()
        val PreviewSpeaking = TongueTwisterExerciseViewState()
    }
}
