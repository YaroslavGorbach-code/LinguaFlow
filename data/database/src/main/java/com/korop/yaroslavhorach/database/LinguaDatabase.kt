package com.korop.yaroslavhorach.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.korop.yaroslavhorach.database.dao.ExerciseProgressDao
import com.korop.yaroslavhorach.database.task.model.ExerciseProgressEntity

@Database(
    entities = [ExerciseProgressEntity::class],
    version = 1,
    exportSchema = true,
)

internal abstract class LinguaDatabase : RoomDatabase() {
    abstract fun exerciseProgressDao(): ExerciseProgressDao
}