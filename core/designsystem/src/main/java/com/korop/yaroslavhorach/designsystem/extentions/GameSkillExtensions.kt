package com.korop.yaroslavhorach.designsystem.extentions

import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.ui.UiText

val Game.Skill.nameString: UiText
    get() = when (this) {
        Game.Skill.CREATIVE -> UiText.FromResource(R.string.game_skill_creativity_text)
        Game.Skill.HUMOR -> UiText.FromResource(R.string.game_skill_humor_text)
        Game.Skill.STORYTELLING -> UiText.FromResource(R.string.game_skill_storytelling_title_text)
        Game.Skill.VOCABULARY -> UiText.FromResource(R.string.game_skill_vocabulary_text)
        Game.Skill.DICTION -> UiText.FromResource(R.string.game_skill_distion_text)
        Game.Skill.FLIRT -> UiText.FromResource(R.string.game_skill_flirt_text)
        else -> UiText.FromString("")
    }
