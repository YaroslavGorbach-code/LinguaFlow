package com.example.yaroslavhorach.domain.exercise_content.model

import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import java.util.Locale

class Situation(
    val id: Long,
    val exerciseName: ExerciseName,
    private val situation: Map<String, String>,
    private val task: Map<String, String>
) {
    private val currentLang = Locale.getDefault().language

    val situationText: String
        get() = situation[currentLang] ?: this.situation["en"] ?: ""

    val taskText: String
        get() = this.task[currentLang] ?: this.task["en"] ?: ""
}