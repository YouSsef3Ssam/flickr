package com.youssef.flickr.business.usecases.abstraction

import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import kotlinx.coroutines.flow.Flow

interface PhotosUseCase {
    suspend fun getPhotos(pageNumber: Int, pageSize: Int): Flow<PhotosResponse>
    suspend fun search(text: String, pageNumber: Int, pageSize: Int): Flow<PhotosResponse>
}