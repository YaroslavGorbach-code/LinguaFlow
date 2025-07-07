package com.korop.yaroslavhorach.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.korop.yaroslavhorach.datastore.ChallengeProgress
import com.korop.yaroslavhorach.datastore.UserPreferences
import com.korop.yaroslavhorach.datastore.challenge.ChallengeSerializer
import com.korop.yaroslavhorach.datastore.prefs.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) {
            context.dataStoreFile("user_preferences.pb")
        }
    }

    @Provides
    @Singleton
    fun provideChallengeProgressDataStore(
        @ApplicationContext context: Context
    ): DataStore<ChallengeProgress> {
        return DataStoreFactory.create(
            serializer = ChallengeSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) {
            context.dataStoreFile("challenge_progress.pb")
        }
    }
}