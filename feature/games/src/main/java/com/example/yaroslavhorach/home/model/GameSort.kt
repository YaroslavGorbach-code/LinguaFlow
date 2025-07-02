package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.games.R
import com.example.yaroslavhorach.ui.UiText

enum class GameSort {
    DAILY_CHALLENGE, FAVORITE, CREATIVE, HUMOR, STORYTELLING, VOCABULARY, DICTION, FLIRT
}

fun getPermanentSorts() = listOf(
    GameSort.CREATIVE,
    GameSort.HUMOR,
    GameSort.STORYTELLING,
    GameSort.VOCABULARY,
    GameSort.DICTION,
    GameSort.FLIRT
)

fun GameSort.getText(): UiText {
   return when (this) {
        GameSort.DAILY_CHALLENGE -> UiText.FromResource(R.string.sort_daily_chalange_text)
        GameSort.FAVORITE -> UiText.FromResource(R.string.sort_favorite_text)
        GameSort.CREATIVE -> UiText.FromResource(R.string.sort_creativity_text)
        GameSort.HUMOR -> UiText.FromResource(R.string.sort_humor_text)
        GameSort.STORYTELLING -> UiText.FromResource(R.string.sort_story_text)
        GameSort.VOCABULARY -> UiText.FromResource(R.string.sort_vocabulary_text)
        GameSort.DICTION -> UiText.FromResource(R.string.sort_diction_text)
        GameSort.FLIRT -> UiText.FromResource(R.string.sort_flirt_text)
    }
}
