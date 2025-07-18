package com.korop.yaroslavhorach.domain.game.model

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

    fun getNameText(lang: String): String {
        return nameString[lang] ?: nameString["en"] ?: ""
    }

    fun getTaskText(lang: String): String {
        return task[lang] ?: task["en"] ?: ""
    }

    fun getExampleText(lang: String): String {
        return example[lang] ?: example["en"] ?: ""
    }

    fun getDescriptionText(lang: String): String {
        return description[lang] ?: description["en"] ?: ""
    }

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
        VOCABULARY,
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
        ONE_LETTER,
        QUICK_ASSOCIATION,
        TONGUE_TWISTERS_EASY,
        TONGUE_TWISTERS_MEDIUM,
        TONGUE_TWISTERS_HARD,
    }
}