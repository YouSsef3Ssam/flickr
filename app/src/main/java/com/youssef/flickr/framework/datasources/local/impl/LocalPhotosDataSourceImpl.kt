package com.youssef.flickr.framework.datasources.local.impl

import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.datasources.local.mappers.LocalPhotoMapper
import com.youssef.flickr.framework.datasources.local.room.daos.PhotosDao
import com.youssef.flickr.framework.presentation.entities.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalPhotosDataSourceImpl @Inject constructor(
    private val dao: PhotosDao,
    private val mapper: LocalPhotoMapper
) : LocalPhotosDataSource {

    override suspend fun getPhotos(): Flow<List<Photo>> =
        dao.getPhotos().map { photos -> photos.map { mapper.mapFrom(it) } }

    override suspend fun getPhotosByIds(ids: List<String>): List<Photo> =
        dao.getPhotosByIds(ids).map { mapper.mapFrom(it) }

    override suspend fun addToFavourite(photo: Photo): Long =
        dao.insertPhoto(mapper.mapTo(photo))

    override suspend fun removeFromFavourite(photo: Photo): Int =
        dao.delete(mapper.mapTo(photo))

}