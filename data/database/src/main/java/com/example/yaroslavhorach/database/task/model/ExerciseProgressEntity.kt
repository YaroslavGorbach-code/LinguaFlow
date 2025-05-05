package com.example.yaroslavhorach.database.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yaroslavhorach.domain.exercise.model.ExerciseProgress

@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntity(
    @PrimaryKey
    val exerciseId: Long,
    val progress: Int,
    val maxProgress: Int
)

fun ExerciseProgressEntity.asDomainModel() = ExerciseProgress(
    exerciseId = exerciseId,
    progress = progress,
    maxProgress = maxProgress
)

fun ExerciseProgress.asEntityModel() = ExerciseProgressEntity(
    exerciseId = exerciseId,
    progress = progress,
    maxProgress = maxProgress
)
