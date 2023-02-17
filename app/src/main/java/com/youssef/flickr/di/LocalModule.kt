package com.youssef.flickr.di

import android.content.Context
import androidx.room.Room
import com.youssef.flickr.framework.datasources.local.room.FlickrDatabase
import com.youssef.flickr.framework.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): FlickrDatabase {
        return Room.databaseBuilder(
            appContext,
            FlickrDatabase::class.java,
            Constants.LocalDataBase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

}