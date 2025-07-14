package com.korop.yaroslavhorach.domain.exercise_content.model

class TongueTwister(
    val id: Long,
    private val text: Map<String, String>
) {

    enum class Difficulty {
        EASY, MEDIUM, HARD
    }

    fun getTextString(lang: String): String {
        return text[lang] ?: text["en"] ?: ""
    }
}