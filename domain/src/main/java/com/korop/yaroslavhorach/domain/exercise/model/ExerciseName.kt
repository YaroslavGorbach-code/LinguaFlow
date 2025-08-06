package com.korop.yaroslavhorach.domain.exercise.model

import com.korop.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.korop.yaroslavhorach.domain.game.model.Game

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
    CHAIN_OF_ASSOCIATIONS,
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
    EMOTIONAL_TRANSLATOR,

    // 6
    CONSPIRACY_BUILDER,
    REVERSE_STORYTELLING,
    MAKE_IT_FASCINATING,
    OVERDRAMATIC_TRIVIALITY,

    // 7
    IRONIC_RESPONSE,
    AWKWARD_SITUATION,
    SUPPORT_WITH_HUMOR,
    IRONIC_DOMESTIC_REACTION,
    FLIRTY_REPLY,
    FLIRT_SUSPECT,
}

fun getExercisesByBlockName(blockName: ExerciseBlock): List<ExerciseName> {
    return when (blockName) {
        ExerciseBlock.ONE -> listOf(
            ExerciseName.ICEBREAKERS,
            ExerciseName.WHAT_TO_SAY_NEXT,
            ExerciseName.THE_KEY_TO_SMALL_TALK,
            ExerciseName.FAREWELL_REMARK
        )
        ExerciseBlock.TWO -> listOf(
            ExerciseName.THREE_SENTENCES,
            ExerciseName.STORY_PUZZLE,
            ExerciseName.SAY_DIFFERENTLY,
            ExerciseName.ONE_MEMORY,
        )
        ExerciseBlock.THREE -> listOf(
            ExerciseName.JOIN_CONVERSATION,
            ExerciseName.ASK_MORE,
            ExerciseName.COMMENT_IN_MOMENT,
            ExerciseName.SMOOTH_RETURN,
            ExerciseName.FIND_THE_TOPIC,
            ExerciseName.I_THINK_SO_TOO
        )
        ExerciseBlock.FOUR -> listOf(
            ExerciseName.GIVE_REASON,
            ExerciseName.WHY_VALUES_MATTER,
            ExerciseName.SOFT_DISAGREEMENT,
            ExerciseName.CALM_POSITION,
            ExerciseName.GENTLE_PERSUASION
        )
        ExerciseBlock.FIVE -> listOf(
            ExerciseName.EMPATHIC_RESPONSE,
            ExerciseName.EMOTIONAL_TRANSLATOR
        )
        ExerciseBlock.SIX -> listOf(
            ExerciseName.CONSPIRACY_BUILDER,
            ExerciseName.REVERSE_STORYTELLING,
            ExerciseName.MAKE_IT_FASCINATING,
            ExerciseName.OVERDRAMATIC_TRIVIALITY
        )
        ExerciseBlock.SEVEN -> listOf(
            ExerciseName.IRONIC_RESPONSE,
            ExerciseName.AWKWARD_SITUATION,
            ExerciseName.SUPPORT_WITH_HUMOR,
            ExerciseName.IRONIC_DOMESTIC_REACTION,
            ExerciseName.FLIRTY_REPLY,
            ExerciseName.FLIRT_SUSPECT
        )
    }
}

fun ExerciseName.mapToTongueTwistDifficulty(): TongueTwister.Difficulty {
    return when (this) {
        ExerciseName.TONGUE_TWISTERS_EASY -> TongueTwister.Difficulty.EASY
        ExerciseName.TONGUE_TWISTERS_MEDIUM -> TongueTwister.Difficulty.MEDIUM
        ExerciseName.TONGUE_TWISTERS_HARD -> TongueTwister.Difficulty.HARD
        else -> error(this.name + "is not a tongue twister")
    }
}

fun ExerciseName.mapToTongueTwistExerciseNameToGameName(): Game.GameName {
    return when (this) {
        ExerciseName.TONGUE_TWISTERS_EASY -> Game.GameName.TONGUE_TWISTERS_EASY
        ExerciseName.TONGUE_TWISTERS_MEDIUM -> Game.GameName.TONGUE_TWISTERS_MEDIUM
        ExerciseName.TONGUE_TWISTERS_HARD ->Game.GameName.TONGUE_TWISTERS_HARD
        else -> error(this.name + "is not a tongue twister")
    }
}