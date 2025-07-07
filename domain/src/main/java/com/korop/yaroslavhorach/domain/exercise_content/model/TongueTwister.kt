package com.korop.yaroslavhorach.domain.exercise_content.model

import java.util.Locale

class TongueTwister(
    val id: Long,
    val difficulty: Difficulty,
    private val text: Map<String, String>,
) {
    enum class Difficulty {
        EASY, MEDIUM, HARD
    }

    private val currentLang = Locale.getDefault().language

    val textString: String
        get() = text[currentLang] ?: text["en"] ?: ""
}