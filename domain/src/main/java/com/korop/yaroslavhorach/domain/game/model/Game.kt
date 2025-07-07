package com.korop.yaroslavhorach.domain.game.model

import java.util.Locale

data class Game(
    val id: Long = 0,
    private val nameString: Map<String, String>,
    private val task: Map<String, String>,
    private val example: Map<String, String>,
    private val description: Map<String, String>,
    val minExperienceRequired: Int = 0,
    val maxProgress: Int = 10,
    val name: GameName,
    val skills: List<Skill>,
) {
    private val currentLang: String
        get() = Locale.getDefault().language

    val nameText: String
        get() = nameString[currentLang] ?: nameString["en"] ?: ""

    val taskText: String
        get() = task[currentLang] ?: task["en"] ?: ""

    val exampleText: String
        get() = example[currentLang] ?: example["en"] ?: ""

    val descriptionText: String
        get() = description[currentLang] ?: description["en"] ?: ""

    enum class Skill {
        CREATIVE, HUMOR, STORYTELLING, VOCABULARY, DICTION, FLIRT
    }

    enum class GameName {
        RAVEN_LIKE_A_CHAIR,
        FOUR_WORDS_ONE_STORY,
        TALK_TILL_EXHAUSTED,
        SELL_THIS_THING,
        DEFINE_PRECISELY,
        BIG_ANSWER,
        EMOTIONAL_TRANSLATOR,
        DEVILS_ADVOCATE,
        DIALOGUE_WITH_SELF,
        IMAGINARY_SITUATION,
        EMOTION_TO_FACT,
        WHO_AM_I_MONOLOGUE,
        I_AM_EXPERT,
        FORBIDDEN_WORDS,
        BODY_LANGUAGE_EXPRESS,
        RAP_IMPROV,
        PERSUASIVE_SHOUT,
        WORD_IN_TEMPO,
        SUBTLE_MANIPULATION,
        ONE_SYNONYM_PLEASE,
        INTONATION_MASTER,
        ANTONYM_BATTLE,
        RHYME_LIGHTNING,
        FUNNIEST_ANSWER,
        MADMAN_ANNOUNCEMENT,
        FUNNY_EXCUSE,
        ONE_WORD_MANY_MEANINGS,
        FLIRTING_WITH_OBJECT,
        BOTH_THERE_AND_IN_BED,
        HOT_WORD,
        TONGUE_TWISTER_EASY,
        TONGUE_TWISTER_MEDIUM,
        TONGUE_TWISTER_HARD,
    }
}