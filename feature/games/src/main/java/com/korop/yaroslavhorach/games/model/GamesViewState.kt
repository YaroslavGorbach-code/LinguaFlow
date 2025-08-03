package com.korop.yaroslavhorach.games.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.domain.game.model.Challenge
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.game.model.Game.GameName
import com.korop.yaroslavhorach.domain.game.model.Game.Skill

data class GamesViewState(
    val gamesForDisplay: List<GameUi>,
    val allGames: List<GameUi>,
    val challenge: Challenge? = null,
    val sorts: List<GameSort> = emptyList(),
    val favorites: List<Long> = emptyList(),
    val selectedSort: GameSort? = null,
    val maxTokens: Int = 0,
    val availableTokens: Int = 0,
    val experience: Int = 0,
    val isUserPremium: Boolean = false,
    val uiMessage: UiMessage<GamesUiMessage>? = null
) {

    companion object {
        private val game = GameUi(
            Game(
                0, nameString = mapOf("en" to "test"),
                task = emptyMap(),
                example = emptyMap(),
                description = mapOf("en" to "test"),
                minExperienceRequired = 0,
                maxProgress = 10,
                name = GameName.HOT_WORD,
                skills = listOf(Skill.CREATIVE, Skill.STORYTELLING),
            )
        )
        private val game2 = GameUi(
            Game(
                0, nameString = mapOf("en" to "test"),
                task = emptyMap(),
                example = emptyMap(),
                description = mapOf("en" to "test"),
                minExperienceRequired = 1000,
                maxProgress = 10,
                name = GameName.HOT_WORD,
                skills = listOf(Skill.CREATIVE, Skill.STORYTELLING),
            )
        )

        val Empty = GamesViewState(emptyList(), emptyList())
        val Preview = GamesViewState(
            listOf(game, game2),
            listOf(game, game2),
            sorts = listOf(GameSort.FLIRT, GameSort.HUMOR)
        )
    }
}
