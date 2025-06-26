package com.example.yaroslavhorach.domain.prefs.model

data class Avatar(
    val resId: Int,
    val userChosen: Boolean = false,
    val isPremium: Boolean = true
)