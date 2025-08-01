
package com.korop.yaroslavhorach.database.di

import android.content.Context
import androidx.room.Room
import com.korop.yaroslavhorach.database.LinguaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesPomoDatabase(@ApplicationContext context: Context): LinguaDatabase = Room.databaseBuilder(
        context,
        LinguaDatabase::class.java,
        "lingua-database",
    ).build()
}
