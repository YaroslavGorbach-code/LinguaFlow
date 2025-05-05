package com.example.yaroslavhorach.database.di

import com.example.yaroslavhorach.database.LinguaDatabase
import com.example.yaroslavhorach.database.dao.ExerciseProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesExerciseProgressDao(database: LinguaDatabase): ExerciseProgressDao {
        return database.exerciseProgressDao()
    }
}
