package com.korop.yaroslavhorach.domain.exercise_content.model

data class Word(
    val id: Long,
    private val word: Map<String, String>,
) {
    fun getWordText(lang: String): String {
        return word[lang] ?: this.word["en"] ?: ""
    }

    enum class WordType {
        NOUN, PLACE, HOT, ANTONIM, ALPHABET
    }
}