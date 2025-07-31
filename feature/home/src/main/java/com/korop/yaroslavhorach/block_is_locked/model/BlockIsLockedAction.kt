package com.korop.yaroslavhorach.block_is_locked.model

sealed class BlockIsLockedAction {
    data object OnBackClicked : BlockIsLockedAction()
    data object OnGoToPremiumClicked : BlockIsLockedAction()
}