package com.korop.yaroslavhorach.profile.model

sealed class ProfileAction {
    data object OnEditProfileClicked: ProfileAction()
    data object OnActivatePremiumClicked: ProfileAction()
    data object OnSettingsClicked: ProfileAction()
}