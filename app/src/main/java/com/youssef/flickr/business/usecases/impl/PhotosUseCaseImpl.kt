package com.youssef.flickr.business.usecases.impl

import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.business.usecases.abstraction.PhotosUseCase
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotosUseCaseImpl @Inject constructor(private val repository: PhotosRepository) :
    PhotosUseCase {
    override suspend fun getPhotos(pageNumber: Int, pageSize: Int): Flow<PhotosResponse> =
        repository.getPhotos(pageNumber, pageSize)

    override suspend fun search(
        text: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<PhotosResponse> = repository.search(text, pageNumber, pageSize)
}
