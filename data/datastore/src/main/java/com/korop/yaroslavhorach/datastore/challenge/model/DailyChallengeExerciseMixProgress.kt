package com.korop.yaroslavhorach.datastore.challenge.model

data class DailyChallengeExerciseMixProgress(
    val id: Long,
    val availableDuringDate: Long,
    val exercisesAndCompleted: List<Pair<String, Boolean>>,
    val isStarted: Boolean
)