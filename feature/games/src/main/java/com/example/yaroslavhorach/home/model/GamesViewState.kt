package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.domain.game.model.Game.GameName
import com.example.yaroslavhorach.domain.game.model.Game.Skill

data class GamesViewState(
    val games: List<GameUi>,
    val sorts: List<GameSort> = emptyList(),
    val favorites: List<Long> = emptyList(),
    val selectedSort: GameSort? = null,
    val maxTokens: Int = 0,
    val availableTokens: Int = 0,
    val experience: Int = 0,
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

        val Empty = GamesViewState(emptyList())
        val Preview = GamesViewState(
            listOf(game),
            listOf(GameSort.FLIRT, GameSort.HUMOR)
        )
    }
}
