package com.korop.yaroslavhorach.avatar_change.model

import com.korop.yaroslavhorach.common.utill.UiMessage
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.domain.prefs.model.Avatar

data class AvatarChangeViewState(
    val userName: String = "",
    val isPremiumUser: Boolean = false,
    val avatarResId: Int? = null,
    val isOnboarding: Boolean = false,
    val avatars: List<Avatar> = emptyList(),
    val uiMessage: UiMessage<AvatarChangeUiMessage>? = null
) {
    companion object {
        val Empty = AvatarChangeViewState()
        val Preview = AvatarChangeViewState(
            userName = "Вовкобой",
            isPremiumUser = false,
            avatarResId = R.drawable.im_avatar_1,
            avatars = listOf(
                Avatar(R.drawable.im_avatar_1),
                Avatar(R.drawable.im_avatar_2),
                Avatar(R.drawable.im_avatar_3),
                Avatar(R.drawable.im_avatar_4),
                Avatar(R.drawable.im_avatar_5),
                Avatar(R.drawable.im_avatar_6),
                Avatar(R.drawable.im_avatar_7)
            )
        )
    }
}