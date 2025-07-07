package com.korop.yaroslavhorach.avatar_change.model

sealed class AvatarChangeUiMessage {
     data object NavigateToHome: AvatarChangeUiMessage()
}