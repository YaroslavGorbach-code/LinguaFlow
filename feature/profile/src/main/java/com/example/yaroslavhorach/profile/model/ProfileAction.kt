package com.example.yaroslavhorach.profile.model

sealed class ProfileAction {
    data object OnEditProfileClicked: ProfileAction()
}