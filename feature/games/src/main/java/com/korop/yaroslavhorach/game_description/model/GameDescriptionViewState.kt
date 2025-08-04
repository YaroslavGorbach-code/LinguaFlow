package com.korop.yaroslavhorach.game_description.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.game.model.Game.GameName
import com.korop.yaroslavhorach.domain.game.model.Game.Skill

data class GameDescriptionViewState(
    val game: GameUi? = null,
    val favorites: List<Long> = emptyList(),
    val isUserPremium: Boolean = false,
    val useToken: Boolean = false,
    val experience: Int = 0,
    val availableTokens: Int = 0,
    val uiMessage: UiMessage<GameDescriptionUiMessage>? = null
) {

    companion object {
        val Empty = GameDescriptionViewState()
        val Preview = GameDescriptionViewState(
            game = GameUi(
                Game(
                    0, nameString = mapOf("en" to "test"),
                    task = emptyMap(),
                    example = emptyMap(),
                    description = mapOf("en" to "test"),
                    minExperienceRequired = 0,
                    maxProgress = 10,
                    name = GameName.HOT_WORD,
                    skills = listOf(Skill.CREATIVE, Skill.STORYTELLING),
                    completedTimes = 1,
                )
            )
        )
        val PreviewLocked = GameDescriptionViewState(
            game = GameUi(
                Game(
                    0, nameString = mapOf("en" to "test"),
                    task = emptyMap(),
                    example = emptyMap(),
                    description = mapOf("en" to "test"),
                    minExperienceRequired = 1000,
                    maxProgress = 10,
                    name = GameName.HOT_WORD,
                    skills = listOf(Skill.CREATIVE, Skill.STORYTELLING),
                    completedTimes = 1,
                )
            )
        )
        val PreviewNoTokens = GameDescriptionViewState(
            availableTokens = 0,
            isUserPremium = false,
            useToken = true,
            game = GameUi(
                Game(
                    0, nameString = mapOf("en" to "test"),
                    task = emptyMap(),
                    example = emptyMap(),
                    description = mapOf("en" to "test"),
                    minExperienceRequired = 1000,
                    maxProgress = 10,
                    name = GameName.HOT_WORD,
                    skills = listOf(Skill.CREATIVE, Skill.STORYTELLING),
                    completedTimes = 1,
                )
            )
        )
    }
}
