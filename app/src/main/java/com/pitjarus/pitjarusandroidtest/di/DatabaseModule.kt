package com.pitjarus.pitjarusandroidtest.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.pitjarus.pitjarusandroidtest.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pitjarus_android_test"
        ).fallbackToDestructiveMigration().build()
    }
}