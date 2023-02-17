package com.youssef.flickr.di

import com.youssef.flickr.business.mappers.PhotoResponseMapper
import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.business.repositories.impl.PhotosRepositoryImpl
import com.youssef.flickr.business.usecases.abstraction.PhotosUseCase
import com.youssef.flickr.business.usecases.impl.PhotosUseCaseImpl
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.datasources.local.impl.LocalPhotosDataSourceImpl
import com.youssef.flickr.framework.datasources.local.mappers.LocalPhotoMapper
import com.youssef.flickr.framework.datasources.local.room.FlickrDatabase
import com.youssef.flickr.framework.datasources.local.room.daos.PhotosDao
import com.youssef.flickr.framework.datasources.remote.abstraction.PhotosDataSource
import com.youssef.flickr.framework.datasources.remote.impl.PhotosDataSourceImpl
import com.youssef.flickr.framework.datasources.remote.services.PhotosApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PhotosModule {

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit): PhotosApi =
        retrofit.create(PhotosApi::class.java)

    @Singleton
    @Provides
    fun providePhotosDao(database: FlickrDatabase): PhotosDao = database.photoDao()

    @Provides
    @Singleton
    fun providePhotosDataSource(api: PhotosApi): PhotosDataSource =
        PhotosDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideLocalPhotosDataSource(
        dao: PhotosDao,
        mapper: LocalPhotoMapper
    ): LocalPhotosDataSource =
        LocalPhotosDataSourceImpl(dao, mapper)

    @Provides
    @Singleton
    fun providePhotosRepository(
        dataSource: PhotosDataSource,
        localDataSource: LocalPhotosDataSource,
        mapper: PhotoResponseMapper
    ): PhotosRepository =
        PhotosRepositoryImpl(dataSource, localDataSource, mapper)

    @Provides
    @Singleton
    fun providePhotoUseCase(repository: PhotosRepository): PhotosUseCase =
        PhotosUseCaseImpl(repository)
}
