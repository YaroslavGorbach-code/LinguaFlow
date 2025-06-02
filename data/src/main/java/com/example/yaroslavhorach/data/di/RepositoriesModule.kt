package com.example.yaroslavhorach.data.di

import com.example.yaroslavhorach.data.exercise.repository.ExerciseRepositoryImpl
import com.example.yaroslavhorach.data.game.GameRepositoryImpl
import com.example.yaroslavhorach.data.exercise_content.repository.ExerciseContentRepositoryImpl
import com.example.yaroslavhorach.data.prefs.repository.PrefsRepositoryImpl
import com.example.yaroslavhorach.domain.exercise.ExerciseRepository
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.game.GameRepository
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindExerciseRepository(exerciseRepository: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    fun bindGameRepository(exerciseRepository: GameRepositoryImpl): GameRepository

    @Binds
    fun bindExerciseContentRepository(exerciseRepository: ExerciseContentRepositoryImpl): ExerciseContentRepository

    @Binds
    fun bindPrefsRepository(prefsRepo: PrefsRepositoryImpl): PrefsRepository
}
