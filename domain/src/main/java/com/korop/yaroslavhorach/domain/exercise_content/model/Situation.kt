package com.korop.yaroslavhorach.domain.exercise_content.model

data class Situation(
    val id: Long,
    private val situation: Map<String, String>,
    private val task: Map<String, String>,
) {
    fun getSituationText(lang: String): String {
        return situation[lang] ?: this.situation["en"] ?: ""
    }

    fun getTaskText(lang: String): String {
        return this.task[lang] ?: this.task["en"] ?: ""
    }
}