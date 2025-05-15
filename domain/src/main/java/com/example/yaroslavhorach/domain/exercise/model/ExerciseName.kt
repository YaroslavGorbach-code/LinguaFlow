package com.example.yaroslavhorach.domain.exercise.model

import com.example.yaroslavhorach.domain.exercise_content.model.TongueTwister

enum class ExerciseName() {
    // Block 1: start conversation
    ICEBREAKERS,
    FINISH_THE_THOUGHT,
    WHAT_TO_SAY_NEXT,
    TONGUE_TWISTERS_EASY,
    THE_KEY_TO_SMALL_TALK,
    DATING_ROUTE,
    VOCABULARY,
    FAREWELL_REMARK,
}

fun ExerciseName.mapToTongueTwistDifficulty(): TongueTwister.Difficulty {
    return when (this) {
        ExerciseName.TONGUE_TWISTERS_EASY -> TongueTwister.Difficulty.EASY
        else -> error(this.name + "is not a tongue twister")
    }
}