package com.example.yaroslavhorach.domain.exercise.model

import com.example.yaroslavhorach.domain.exercise_content.model.TongueTwister

enum class ExerciseName() {
    // Block 1: start conversation
    ICEBREAKERS,
    FINISH_THE_THOUGHT,
    WHAT_TO_SAY_NEXT,
    TONGUE_TWISTERS_EASY,
    TONGUE_TWISTERS_MEDIUM,
    TONGUE_TWISTERS_HARD,
    THE_KEY_TO_SMALL_TALK,
    DATING_ROUTE,
    VOCABULARY,
    FAREWELL_REMARK,

    // 2
    THREE_SENTENCES,
    DETAILED_STORYTELLING,
    STORY_PUZZLE,
    SAY_DIFFERENTLY,
    ONE_MEMORY,

    // 3
    JOIN_CONVERSATION,
    ASK_MORE,
    COMMENT_IN_MOMENT,
    SMOOTH_RETURN,
    FIND_THE_TOPIC,
    I_THINK_SO_TOO,

    // 4
    GIVE_REASON,
    WHY_VALUES_MATTER,
    SOFT_DISAGREEMENT,
    CALM_POSITION,
    GENTLE_PERSUASION,

    // 5
    EMPATHIC_RESPONSE,
    EMOTIONAL_TRANSLATOR
}

fun ExerciseName.mapToTongueTwistDifficulty(): TongueTwister.Difficulty {
    return when (this) {
        ExerciseName.TONGUE_TWISTERS_EASY -> TongueTwister.Difficulty.EASY
        ExerciseName.TONGUE_TWISTERS_MEDIUM -> TongueTwister.Difficulty.MEDIUM
        ExerciseName.TONGUE_TWISTERS_HARD -> TongueTwister.Difficulty.HARD
        else -> error(this.name + "is not a tongue twister")
    }
}