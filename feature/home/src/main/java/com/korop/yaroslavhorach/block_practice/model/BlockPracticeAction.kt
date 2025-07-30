package com.korop.yaroslavhorach.block_practice.model

sealed class BlockPracticeAction {
    data object OnBackClicked : BlockPracticeAction()
    data object OnStartTrainingClicked : BlockPracticeAction()
}