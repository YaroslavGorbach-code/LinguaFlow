package com.korop.yaroslavhorach.data.di

import com.korop.yaroslavhorach.data.exercise.repository.ExerciseRepositoryImpl
import com.korop.yaroslavhorach.data.game.GameRepositoryImpl
import com.korop.yaroslavhorach.data.exercise_content.repository.ExerciseContentRepositoryImpl
import com.korop.yaroslavhorach.data.prefs.repository.PrefsRepositoryImpl
import com.korop.yaroslavhorach.domain.exercise.ExerciseRepository
import com.korop.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.korop.yaroslavhorach.domain.game.GameRepository
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
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
