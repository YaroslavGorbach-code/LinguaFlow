package com.example.yaroslavhorach.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.yaroslavhorach.database.task.model.ExerciseProgressEntity
import com.example.yaroslavhorach.domain.exercise.model.ExerciseProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgressDao {
    @Query(
        value = """
        SELECT * FROM exercise_progress
    """,
    )
    fun getExerciseProgressEntities(): Flow<List<ExerciseProgressEntity>>

    @Query(
        value = """
        SELECT * FROM exercise_progress
        WHERE exerciseId = :exerciseId
    """,
    )
    fun getExerciseProgressEntity(exerciseId: Long): Flow<ExerciseProgressEntity>

    @Upsert
    suspend fun upsertExerciseProgress(exerciseProgress: ExerciseProgressEntity)

    @Query(
        value = """
            DELETE FROM exercise_progress
            WHERE exerciseId in (:ids)
        """,
    )
    suspend fun deleteTasks(ids: List<Long>)
}
