package com.example.yaroslavhorach.domain.exercise_content.model

import java.util.Locale

class Word(
    val id: Long,
    val exerciseName: WordType,
    private val word: Map<String, String>,
) {
    private val currentLang = Locale.getDefault().language

    val wordText: String
        get() = word[currentLang] ?: this.word["en"] ?: ""

    enum class WordType {
        NOUN, PLACE, HOT, ANTONIM
    }
}