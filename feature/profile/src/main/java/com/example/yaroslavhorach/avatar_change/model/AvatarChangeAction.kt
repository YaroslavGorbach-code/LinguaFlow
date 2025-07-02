package com.example.yaroslavhorach.avatar_change.model

sealed class AvatarChangeAction {
    data class OnAvatarChosen(val resId: Int): AvatarChangeAction()
    data class OnNameTyped(val name: String): AvatarChangeAction()
    data object OnBackClicked: AvatarChangeAction()
    data object OnPremiumClicked: AvatarChangeAction()
    data object OnNextClicked: AvatarChangeAction()
}