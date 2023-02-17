package com.youssef.flickr.business.repositories.abstraction

import com.youssef.flickr.framework.presentation.entities.Photo
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    suspend fun getFavouritePhotos(): Flow<List<Photo>>
    suspend fun addToFavourite(photo: Photo): Flow<Long>
    suspend fun removeFromFavourite(photo: Photo): Flow<Int>
    suspend fun downloadPhoto(url: String): Flow<String>
}