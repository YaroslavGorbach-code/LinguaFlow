package com.example.yaroslavhorach.datastore.challenge.model

data class DailyChallengeProgress(
    val id: Long,
    val availableDuringDate: Long,
    val progressInMs: Long,
    val isStarted: Boolean
)