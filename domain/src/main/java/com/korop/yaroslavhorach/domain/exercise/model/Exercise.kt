package com.korop.yaroslavhorach.domain.exercise.model

import java.util.Locale

data class Exercise(
    val id: Long = 0,
    private val nameString: HashMap<String, String>,
    private val description: HashMap<String, String>,
    val exerciseProgress: ExerciseProgress,
    val name: ExerciseName,
    val skill: Skill,
    val block: ExerciseBlock,
    val isEnable: Boolean = true,
    val isLastActive: Boolean = false,
    val isVisible: Boolean = true
){
    private val currentLang = Locale.getDefault().language

    val nameText: String
        get() = nameString[currentLang] ?: this.nameString["en"] ?: ""

    val descriptionText: String
        get() = description[currentLang] ?: this.description["en"] ?: ""
}