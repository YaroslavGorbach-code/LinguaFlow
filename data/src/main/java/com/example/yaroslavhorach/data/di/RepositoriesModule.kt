package com.example.yaroslavhorach.data.di

import com.example.yaroslavhorach.data.exercise.repository.ExerciseRepositoryImpl
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindExerciseRepository(exerciseRepository: ExerciseRepositoryImpl): ExerciseRepository
}
