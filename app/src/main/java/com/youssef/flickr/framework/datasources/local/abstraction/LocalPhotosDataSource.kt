package com.youssef.flickr.framework.datasources.local.abstraction

import com.youssef.flickr.framework.presentation.entities.Photo
import kotlinx.coroutines.flow.Flow

interface LocalPhotosDataSource {
    suspend fun getPhotos(): Flow<List<Photo>>
    suspend fun getPhotosByIds(ids: List<String>): List<Photo>
    suspend fun addToFavourite(photo: Photo): Long
    suspend fun removeFromFavourite(photo: Photo): Int
}
