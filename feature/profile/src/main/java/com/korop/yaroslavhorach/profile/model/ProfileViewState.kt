package com.korop.yaroslavhorach.profile.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.ui.SpeakingLevel
import java.util.Calendar

data class ProfileViewState(
    val userName: String = "Вікторія",
    val isPremiumUser: Boolean = false,
    val lasActiveDays: List<CalendarDay> = emptyList(),
    val activeDays: Int = 0,
    val experience: Int = 0,
    val levelOfSpeaking: SpeakingLevel? = null,
    val activeDaysInRow: Int = 0,
    val avatarResId: Int? = null,
    val uiMessage: UiMessage<ProfileUiMessage>? = null
) {
    companion object {
        val Empty = ProfileViewState()
        val Preview = ProfileViewState(
            userName = "Вікторія",
            isPremiumUser = false,
            lasActiveDays = listOf(
                CalendarDay(Calendar.getInstance().timeInMillis, false),
                CalendarDay(Calendar.getInstance().timeInMillis, true)
            ),
            activeDays = 22,
            activeDaysInRow = 5,
            avatarResId = R.drawable.im_avatar_1,
            levelOfSpeaking = SpeakingLevel.Master()
        )
    }
}