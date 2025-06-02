package com.example.yaroslavhorach.designsystem.extentions

import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.ui.UiText

val Game.Skill.nameString: UiText
    get() = when (this) {
        Game.Skill.CREATIVE -> UiText.FromString("креативність")
        Game.Skill.HUMOR -> UiText.FromString("гумор")
        Game.Skill.STORYTELLING -> UiText.FromString("розповідь історій")
        Game.Skill.VOCABULARY -> UiText.FromString("словниковий запас")
        Game.Skill.DICTION -> UiText.FromString("дикцію")
        Game.Skill.FLIRT -> UiText.FromString("флірт")
        else -> UiText.FromString("")
    }
