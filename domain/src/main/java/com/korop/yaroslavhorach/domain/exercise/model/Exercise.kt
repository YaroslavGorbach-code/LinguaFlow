package com.korop.yaroslavhorach.domain.exercise.model

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
    val isVisible: Boolean = true,
) {
    fun getNameText(lang: String): String {
        return nameString[lang] ?: this.nameString["en"] ?: ""
    }

    fun getDescriptionText(lang: String): String {
        return description[lang] ?: this.description["en"] ?: ""
    }
}