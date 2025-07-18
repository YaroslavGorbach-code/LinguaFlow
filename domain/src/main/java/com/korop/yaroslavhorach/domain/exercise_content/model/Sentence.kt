package com.korop.yaroslavhorach.domain.exercise_content.model

data class Sentence(
    val id: Long,
    private val sentence: Map<String, String>,
) {

    fun getText(lang: String): String {
        return sentence[lang] ?: this.sentence["en"] ?: ""
    }

    enum class SentenceType {
        SIMPLE_QUESTION,
        EMOTIONAL_TRANSLATION,
        DEVILS_ADVOCATE,
        DIALOGUE_WITH_SELF,
        IMAGINARY_SITUATION,
        EMOTION_TO_FACT,
        WHO_AM_I,
        I_AM_EXPERT,
        FORBIDDEN_WORDS,
        BODY_LANGUAGE,
        PERSUASIVE_SHOUT,
        SUBTLE_MANIPULATION,
        ONE_SYNONYM_PLEASE,
        INTONATION_MASTER,
        FUNNIEST_ANSWER,
        SELL_THE_MADNESS,
        FUNNY_EXCUSE,
        PROBLEMS
    }
}