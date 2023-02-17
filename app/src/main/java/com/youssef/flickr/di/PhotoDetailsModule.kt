package com.youssef.flickr.di

import android.content.Context
import com.youssef.flickr.business.repositories.abstraction.FavouritesRepository
import com.youssef.flickr.business.repositories.impl.FavouritesRepositoryImpl
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.business.usecases.impl.FavouritesUseCaseImpl
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PhotoDetailsModule {


    @Provides
    @Singleton
    fun provideFavouritesRepository(
        @ApplicationContext context: Context,
        dataSource: LocalPhotosDataSource
    ): FavouritesRepository =
        FavouritesRepositoryImpl(context, dataSource)

    @Provides
    @Singleton
    fun provideFavouritesUseCase(repository: FavouritesRepository): FavouritesUseCase =
        FavouritesUseCaseImpl(repository)

}