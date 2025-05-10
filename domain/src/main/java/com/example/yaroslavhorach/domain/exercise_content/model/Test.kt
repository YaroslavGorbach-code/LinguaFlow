package com.example.yaroslavhorach.domain.exercise_content.model

import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import java.util.Locale

data class Test(
    val id: Long,
    val exerciseName: ExerciseName,
    val situation: Map<String, String>,
    val task: Map<String, String>,
    private val correctAnswer: Map<String, String>,
    private val wrongAnswer: Map<String, String>,
    val variants: List<Variant>
) {
    private val currentLang = Locale.getDefault().language

     class Variant(
        private val texts: Map<String, String>,
        val isCorrect: Boolean
    ) {
         private val currentLang = Locale.getDefault().language

         val variantText: String
            get() = texts[currentLang] ?: this.texts["en"] ?: ""
    }

    val situationText: String
        get() = situation[currentLang] ?: this.situation["en"] ?: ""

    val taskText: String
        get() = this.task[currentLang] ?: this.task["en"] ?: ""

    val wrongAnswerExplanation: String
        get() = wrongAnswer[currentLang] ?: this.wrongAnswer["en"] ?: ""

    val correctAnswerExplanation: String
        get() = correctAnswer[currentLang] ?: this.correctAnswer["en"] ?: ""
}

