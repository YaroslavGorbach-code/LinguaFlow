package com.korop.yaroslavhorach.domain.exercise_content.model

data class Vocabulary(
    val id: Long,
    val type: WordType,
    private val task: Map<String, String>,
    private val example: Map<String, String>,
    val wrongAnswer: ScoreFeedback,
    val notBadAnswer: ScoreFeedback,
    val correctAnswer: ScoreFeedback,
) {
    enum class WordType {
        NOUN, ADJECTIVE, VERB
    }

    fun getTaskText(lang: String): String {
        return task[lang] ?: task["en"] ?: ""
    }

    fun getExampleText(lang: String): String {
        return example[lang] ?: example["en"] ?: ""
    }

    fun getFeedback(value: Int, lan: String): FeedBack {
        return when (value) {
            in wrongAnswer.minValue..wrongAnswer.maxValue -> {
                FeedBack.Bad(wrongAnswer.getFeedbackText(lan))
            }

            in notBadAnswer.minValue..notBadAnswer.maxValue -> {
                FeedBack.NotBad(notBadAnswer.getFeedbackText(lan))
            }

            in correctAnswer.minValue..correctAnswer.maxValue -> {
                FeedBack.Good(correctAnswer.getFeedbackText(lan))
            }

            else -> FeedBack.Good(correctAnswer.getFeedbackText(lan))
        }
    }

    fun getProgressRangeValues(): List<Int> {
        return listOf(wrongAnswer.maxValue, notBadAnswer.maxValue, correctAnswer.maxValue)
    }

    inner class ScoreFeedback(
        val minValue: Int,
        val maxValue: Int,
        private val text: Map<String, String>,
    ) {
        fun getFeedbackText(lang: String): String {
            return text[lang] ?: text["en"] ?: ""
        }
    }
}

sealed class FeedBack(open val text: String) {
    class Good(override val text: String) : FeedBack(text)
    class NotBad(override val text: String) : FeedBack(text)
    class Bad(override val text: String) : FeedBack(text)
}