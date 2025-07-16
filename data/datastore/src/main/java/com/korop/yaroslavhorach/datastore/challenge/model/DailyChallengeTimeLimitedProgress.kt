package com.korop.yaroslavhorach.datastore.challenge.model

data class DailyChallengeTimeLimitedProgress(
    val id: Long,
    val availableDuringDate: Long,
    val progressInMs: Long,
    val isStarted: Boolean
)