package com.youssef.flickr.business.repositories.abstraction

import com.youssef.flickr.business.entities.PhotosResponseDto
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    suspend fun getPhotos(pageNumber: Int, pageSize: Int): Flow<PhotosResponse>
    suspend fun search(text: String, pageNumber: Int, pageSize: Int): Flow<PhotosResponse>
    suspend fun applyFavourites(responseDto: PhotosResponseDto): PhotosResponse
}
