package com.korop.yaroslavhorach.domain.game.model

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
    val progressInMinutes: Int,
) {
    fun getTitle(lang: String): String {
        return titleText[lang] ?: titleText["en"] ?: ""
    }

    fun getDescription(lang: String): String {
        return descriptionText[lang] ?: descriptionText["en"] ?: ""
    }

    fun getAcceptMessage(lang: String): String {
        return acceptText[lang] ?: acceptText["en"] ?: ""
    }

    fun getCompleteMessage(lang: String): String {
        return completeText[lang] ?: completeText["en"] ?: ""
    }

    data class Status(
        val started: Boolean = false,
        val completed: Boolean = false
    ) {
        val inProgress: Boolean
            get() = started && completed.not()
    }
}