package com.korop.yaroslavhorach.domain.game.model

import java.util.Locale

data class Challenge(
    val id: Long = 0,
    private val titleText: Map<String, String>,
    private val descriptionText: Map<String, String>,
    private val acceptText: Map<String, String>,
    private val completeText: Map<String, String>,
    val theme: Game.Skill,
    val durationMinutes: Int,
    val tokenCost: Int,
    val bonusOnComplete: Int,
    val status: Status = Status(),
    val progressInMinutes: Int
) {
    private val currentLang: String
        get() = Locale.getDefault().language

    val title: String
        get() = titleText[currentLang] ?: titleText["en"] ?: ""

    val description: String
        get() = descriptionText[currentLang] ?: descriptionText["en"] ?: ""

    val acceptMessage: String
        get() = acceptText[currentLang] ?: acceptText["en"] ?: ""

    val completeMessage: String
        get() = completeText[currentLang] ?: completeText["en"] ?: ""

    data class Status(
        val started: Boolean = false,
        val completed: Boolean = false
    ) {
        val inProgress: Boolean
            get() = started && completed.not()
    }
}