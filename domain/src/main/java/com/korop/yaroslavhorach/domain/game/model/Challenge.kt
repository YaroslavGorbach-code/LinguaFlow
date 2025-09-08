package com.korop.yaroslavhorach.domain.game.model

sealed class Challenge(
    open val id: Long = 0,
    open val titleText: Map<String, String>,
    open val descriptionText: Map<String, String>,
    open val acceptText: Map<String, String>,
    open val completeText: Map<String, String>,
    open val tokenCost: Int,
    open val bonusOnComplete: Int,
    open var status: Status = Status(),
) {
    fun getTitle(lang: String): String {
        return titleText[lang] ?: titleText["en"] ?: ""
    }

    fun getDescription(lang: String, arguments: String = ""): String {
        return descriptionText[lang]?.replace("{{exerciseList}}", arguments) ?: ""
    }

    fun getAcceptMessage(lang: String, arguments: String = ""): String {
        return acceptText[lang]?.replace("{{exerciseList}}", arguments) ?: ""
    }

    fun getCompleteMessage(lang: String): String {
        return completeText[lang] ?: titleText["en"] ?: ""
    }

    data class Status(
        val started: Boolean = false,
        val completed: Boolean = false,
    ) {
        val inProgress: Boolean
            get() = started && completed.not()
    }
}