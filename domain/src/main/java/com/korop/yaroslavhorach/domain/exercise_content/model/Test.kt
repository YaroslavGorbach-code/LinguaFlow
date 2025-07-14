package com.korop.yaroslavhorach.domain.exercise_content.model

data class Test(
    val id: Long,
    val situation: Map<String, String>,
    val task: Map<String, String>,
    private val correctAnswer: Map<String, String>,
    private val wrongAnswer: Map<String, String>,
    val variants: List<Variant>,
) {
    inner class Variant(
        private val texts: Map<String, String>,
        val isCorrect: Boolean
    ) {
        fun getVariantText(lang: String): String {
            return texts[lang] ?: this.texts["en"] ?: ""
        }
    }

    fun getSituationTex(lang: String): String {
        return situation[lang] ?: this.situation["en"] ?: ""
    }

    fun getTaskText(lang: String): String {
        return this.task[lang] ?: this.task["en"] ?: ""
    }

    fun getWrongAnswerExplanation(lang: String): String {
        return wrongAnswer[lang] ?: this.wrongAnswer["en"] ?: ""
    }

    fun getCorrectAnswerExplanation(lang: String): String {
        return correctAnswer[lang] ?: this.correctAnswer["en"] ?: ""
    }
}

