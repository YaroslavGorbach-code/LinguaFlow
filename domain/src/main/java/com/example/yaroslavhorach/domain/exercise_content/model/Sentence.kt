package com.example.yaroslavhorach.domain.exercise_content.model

import java.util.Locale

class Sentence(
    val id: Long,
    val sentenceType: SentenceType,
    private val sentence: Map<String, String>,
) {
    private val currentLang = Locale.getDefault().language

    val text: String
        get() = sentence[currentLang] ?: this.sentence["en"] ?: ""

    enum class SentenceType {
        SIMPLE_QUESTION,
        EMOTIONAL_TRANSLATION,
        DEVILS_ADVOCATE,
        DIALOGUE_WITH_SELF,
        IMAGINARY_SITUATION,
        EMOTION_TO_FACT,
        WHO_AM_I,
        I_AM_EXPERT,
        FORBIDDEN_WORDS
    }
}