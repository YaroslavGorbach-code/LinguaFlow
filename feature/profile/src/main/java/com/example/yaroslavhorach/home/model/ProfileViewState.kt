package com.example.yaroslavhorach.home.model

import com.example.yaroslavhorach.common.utill.UiMessage

data class ProfileViewState(
    val userName: String = "Вікторія",
    val isPremiumUser: Boolean = false,
    val lastSevenDays: List<CalendarDay> = emptyList(),
    val activeDays: Int = 0,
    val activeDaysInRow: Int = 0,
    val avatarResId: Int = com.example.yaroslavhorach.designsystem.R.drawable.im_avatar_1,
    val uiMessage: UiMessage<ProfileUiMessage>? = null
) {
    companion object {
        val Empty = ProfileViewState()
        val Preview = ProfileViewState(
            userName = "Вікторія",
            isPremiumUser = false,
            lastSevenDays = emptyList(),
            activeDays = 22,
            activeDaysInRow = 5,
            avatarResId = com.example.yaroslavhorach.designsystem.R.drawable.im_avatar_1
        )
    }
}