package com.korop.yaroslavhorach.home.model

import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.extentions.nameString
import com.korop.yaroslavhorach.domain.game.model.Game

data class GameUi(
    val game: Game,
    val isDescriptionVisible: Boolean = false,
    val isChallengeGame: Boolean = false,
    val isChallengeGameCompleted: Boolean = false
) {

    val iconResId: Int = when (game.name) {
        Game.GameName.RAVEN_LIKE_A_CHAIR -> R.drawable.im_raven
        Game.GameName.FOUR_WORDS_ONE_STORY -> R.drawable.im_reading
        Game.GameName.TALK_TILL_EXHAUSTED -> R.drawable.im_talk_till_exhausted
        Game.GameName.SELL_THIS_THING -> R.drawable.im_sell_me_this
        Game.GameName.DEFINE_PRECISELY -> R.drawable.im_do_not_confuce_me
        Game.GameName.BIG_ANSWER -> R.drawable.im_big_answer
        Game.GameName.EMOTIONAL_TRANSLATOR -> R.drawable.im_emotions
        Game.GameName.DEVILS_ADVOCATE -> R.drawable.im_advocate
        Game.GameName.DIALOGUE_WITH_SELF -> R.drawable.im_talk
        Game.GameName.IMAGINARY_SITUATION -> R.drawable.im_imagination
        Game.GameName.EMOTION_TO_FACT -> R.drawable.im_facts
        Game.GameName.WHO_AM_I_MONOLOGUE -> R.drawable.im_superheroe
        Game.GameName.I_AM_EXPERT -> R.drawable.im_expert
        Game.GameName.FORBIDDEN_WORDS -> R.drawable.im_forbiden
        Game.GameName.BODY_LANGUAGE_EXPRESS -> R.drawable.im_body_language
        Game.GameName.RAP_IMPROV -> R.drawable.im_rap
        Game.GameName.PERSUASIVE_SHOUT -> R.drawable.im_bloger
        Game.GameName.VOCABULARY -> R.drawable.im_scrabble
        Game.GameName.SUBTLE_MANIPULATION -> R.drawable.im_manipulation
        Game.GameName.ONE_SYNONYM_PLEASE -> R.drawable.im_equal
        Game.GameName.INTONATION_MASTER -> R.drawable.im_intonation
        Game.GameName.ANTONYM_BATTLE -> R.drawable.im_sword
        Game.GameName.RHYME_LIGHTNING -> R.drawable.im_lighting
        Game.GameName.FUNNIEST_ANSWER -> R.drawable.im_balloons
        Game.GameName.MADMAN_ANNOUNCEMENT -> R.drawable.im_promote
        Game.GameName.FUNNY_EXCUSE -> R.drawable.im_clown
        Game.GameName.ONE_WORD_MANY_MEANINGS -> R.drawable.im_lettering
        Game.GameName.FLIRTING_WITH_OBJECT -> R.drawable.im_lips
        Game.GameName.BOTH_THERE_AND_IN_BED -> R.drawable.im_bed
        Game.GameName.HOT_WORD -> R.drawable.im_fire
        Game.GameName.ONE_LETTER -> R.drawable.ic_alphabet
        Game.GameName.TONGUE_TWISTERS_EASY -> R.drawable.im_tongue_easy
        Game.GameName.TONGUE_TWISTERS_MEDIUM -> R.drawable.im_tongue_medium
        Game.GameName.TONGUE_TWISTERS_HARD -> R.drawable.im_tongue_hard
    }

    val skills = game.skills.map { it.nameString }
}