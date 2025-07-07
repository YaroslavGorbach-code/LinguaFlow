package com.korop.yaroslavhorach.ui

import com.korop.yaroslavhorach.common.R


sealed class SpeakingLevel(
    val level: Int,
    val experienceRequired: IntRange,
    val title: UiText,
    val description: UiText,
    val emoji: String
) {

    companion object {
        fun nextLevel(currentLevel: Int): SpeakingLevel? = when (currentLevel) {
            1 -> Conversationalist()
            2 -> Communicator()
            3 -> Storyteller()
            4 -> ConfidentSpeaker()
            5 ->  Master()
            else -> null
        }

        fun fromExperience(exp: Int): SpeakingLevel =
            allLevels.find { exp in it.experienceRequired } ?: Listener()

        private val allLevels: List<SpeakingLevel> = listOf(
            Listener(), Conversationalist(), Communicator(),
            Storyteller(), ConfidentSpeaker(), Master()
        )
    }

    class Listener : SpeakingLevel(
        level = 1,
        experienceRequired = 0..500,
        title = UiText.FromResource(R.string.speaking_level_1_title_text),
        emoji = "🧱",
        description = UiText.FromResource(R.string.speaking_level_1_subtitle_text)
    )

    class Conversationalist : SpeakingLevel(
        level = 2,
        experienceRequired = 500..1000,
        title = UiText.FromResource(R.string.speaking_level_2_title_text),
        emoji = "💬",
        description = UiText.FromResource(R.string.speaking_level_2_subtitle_text)
    )

    class Communicator : SpeakingLevel(
        level = 3,
        experienceRequired = 1000..2000,
        title = UiText.FromResource(R.string.speaking_level_3_title_text),
        emoji = "🧭",
        description = UiText.FromResource(R.string.speaking_level_3_subtitle_text)
    )

    class Storyteller : SpeakingLevel(
        level = 4,
        experienceRequired = 2000..3000,
        title = UiText.FromResource(R.string.speaking_level_4_title_text),
        emoji = "🎙️",
        description = UiText.FromResource(R.string.speaking_level_4_subtitle_text)
    )

    class ConfidentSpeaker : SpeakingLevel(
        level = 5,
        experienceRequired = 3000..4000,
        title = UiText.FromResource(R.string.speaking_level_5_title_text),
        emoji = "🔥",
        description = UiText.FromResource(R.string.speaking_level_5_description_text)
    )

    class Master : SpeakingLevel(
        level = 6,
        experienceRequired = 4000..5000,
        title = UiText.FromResource(R.string.speaking_level_6_title_text),
        emoji = "🦾",
        description = UiText.FromResource(R.string.speaking_level_6_subtitle_text)
    )

}