package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.designsystem.extentions.nameString
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons.RavenLikeAChair
import com.example.yaroslavhorach.domain.game.model.Game

data class GameUi(val game: Game, val isDescriptionVisible: Boolean = false) {

    val iconResId: Int = when (game.name) {
        Game.GameName.RAVEN_LIKE_A_CHAIR -> RavenLikeAChair
        Game.GameName.FOUR_WORDS_ONE_STORY -> RavenLikeAChair
        Game.GameName.TALK_TILL_EXHAUSTED -> RavenLikeAChair
        Game.GameName.SELL_THIS_THING -> RavenLikeAChair
        Game.GameName.DEFINE_PRECISELY -> RavenLikeAChair
        Game.GameName.BIG_ANSWER -> RavenLikeAChair
        Game.GameName.EMOTIONAL_TRANSLATOR -> RavenLikeAChair
        Game.GameName.DEVILS_ADVOCATE -> RavenLikeAChair
        Game.GameName.DIALOGUE_WITH_SELF -> RavenLikeAChair
        Game.GameName.IMAGINARY_SITUATION -> RavenLikeAChair
        Game.GameName.EMOTION_TO_FACT -> RavenLikeAChair
        Game.GameName.WHO_AM_I_MONOLOGUE -> RavenLikeAChair
        Game.GameName.I_AM_EXPERT -> RavenLikeAChair
        Game.GameName.FORBIDDEN_WORDS -> RavenLikeAChair
        Game.GameName.BODY_LANGUAGE_EXPRESS -> RavenLikeAChair
        Game.GameName.RAP_IMPROV -> RavenLikeAChair
        Game.GameName.PERSUASIVE_SHOUT -> RavenLikeAChair
        Game.GameName.WORD_IN_TEMPO -> RavenLikeAChair
        Game.GameName.SUBTLE_MANIPULATION -> RavenLikeAChair
        Game.GameName.ONE_SYNONYM_PLEASE -> RavenLikeAChair
        Game.GameName.INTONATION_MASTER -> RavenLikeAChair
        Game.GameName.ANTONYM_BATTLE -> RavenLikeAChair
        Game.GameName.RHYME_LIGHTNING -> RavenLikeAChair
        Game.GameName.FUNNIEST_ANSWER -> RavenLikeAChair
        Game.GameName.MADMAN_ANNOUNCEMENT -> RavenLikeAChair
        Game.GameName.FUNNY_EXCUSE -> RavenLikeAChair
        Game.GameName.ONE_WORD_MANY_MEANINGS -> RavenLikeAChair
        Game.GameName.INTONATION_MATTERS -> RavenLikeAChair
        Game.GameName.FLIRTING_WITH_OBJECT -> RavenLikeAChair
        Game.GameName.BOTH_THERE_AND_IN_BED -> RavenLikeAChair
        Game.GameName.HOT_WORD -> RavenLikeAChair
        Game.GameName.DOUBLE_MEANING_WORDS -> RavenLikeAChair
    }

    // TODO: is enable implement
    val isEnable = game.minExperienceRequired == 0

    val skills = game.skills.map { it.nameString }
}