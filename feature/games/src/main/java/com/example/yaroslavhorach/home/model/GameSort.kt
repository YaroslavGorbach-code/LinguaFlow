package com.example.yaroslavhorach.home.model

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
        GameSort.DAILY_CHALLENGE -> UiText.FromString("\uD83D\uDD25 Виклик дня")
        GameSort.FAVORITE -> UiText.FromString("⭐ Улюблене")
        GameSort.CREATIVE -> UiText.FromString("\uD83E\uDDE0 Креативність")
        GameSort.HUMOR -> UiText.FromString("\uD83D\uDE04 Почуття гумору")
        GameSort.STORYTELLING -> UiText.FromString("\uD83D\uDCD6 Сторітелінг")
        GameSort.VOCABULARY -> UiText.FromString("\uD83D\uDDE3\uFE0F Словниковий запас")
        GameSort.DICTION -> UiText.FromString("\uD83D\uDC44 Дикція")
        GameSort.FLIRT -> UiText.FromString("\uD83D\uDC98 Флірт")
    }
}
