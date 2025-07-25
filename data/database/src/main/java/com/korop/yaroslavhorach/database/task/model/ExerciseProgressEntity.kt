package com.korop.yaroslavhorach.database.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseProgress

@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntity(
    @PrimaryKey
    val exerciseId: Long,
    val progress: Int,
    val maxProgress: Int,
) {
    val isFinished
        get() = progress >= maxProgress
}

fun ExerciseProgressEntity.asDomainModel() = ExerciseProgress(
    exerciseId = exerciseId,
    progress = progress,
    maxProgress = maxProgress,
)

fun ExerciseProgress.asEntityModel() = ExerciseProgressEntity(
    exerciseId = exerciseId,
    progress = progress,
    maxProgress = maxProgress,
)
