package com.korop.yaroslavhorach.domain.exercise_content.model

import java.util.Locale

data class Vocabulary(
    val id: Long,
    val exerciseName: String,
    val type: WordType,
    private val task: Map<String, String>,
    private val example: Map<String, String>,
    val wrongAnswer: ScoreFeedback,
    val notBadAnswer: ScoreFeedback,
    val correctAnswer: ScoreFeedback
) {
    enum class WordType {
        NOUN, ADJECTIVE, VERB
    }

    private val currentLang: String
        get() = Locale.getDefault().language

    val taskText: String
        get() = task[currentLang] ?: task["en"] ?: ""

    val exampleText: String
        get() = example[currentLang] ?: example["en"] ?: ""

    fun getFeedback(value: Int): FeedBack {
        return when (value) {
            in wrongAnswer.minValue..wrongAnswer.maxValue -> {
                FeedBack.Bad(wrongAnswer.feedbackText)
            }
            in notBadAnswer.minValue..notBadAnswer.maxValue -> {
                FeedBack.NotBad(notBadAnswer.feedbackText)
            }
            in correctAnswer.minValue..correctAnswer.maxValue -> {
                FeedBack.Good(correctAnswer.feedbackText)
            }
            else -> FeedBack.Good(correctAnswer.feedbackText)
        }
    }

    fun getProgressRangeValues(): List<Int> {
        return listOf(wrongAnswer.maxValue, notBadAnswer.maxValue, correctAnswer.maxValue)
    }
}

data class ScoreFeedback(
    val minValue: Int,
    val maxValue: Int,
    private val text: Map<String, String>
) {
    private val currentLang: String
        get() = Locale.getDefault().language

    val feedbackText: String
        get() = text[currentLang] ?: text["en"] ?: ""
}

sealed class FeedBack(open val text: String) {
    class Good(override val text: String) : FeedBack(text)
    class NotBad(override val text: String) : FeedBack(text)
    class Bad(override val text: String) : FeedBack(text)
}