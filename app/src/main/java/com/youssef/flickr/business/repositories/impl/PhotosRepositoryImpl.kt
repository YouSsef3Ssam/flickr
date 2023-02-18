package com.youssef.flickr.business.repositories.impl

import com.youssef.flickr.business.entities.PhotosResponseDto
import com.youssef.flickr.business.mappers.PhotoResponseMapper
import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.datasources.remote.abstraction.PhotosDataSource
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val dataSource: PhotosDataSource,
    private val localDataSource: LocalPhotosDataSource,
    private val mapper: PhotoResponseMapper
) : PhotosRepository {

    override suspend fun getPhotos(pageNumber: Int, pageSize: Int): Flow<PhotosResponse> = flow {
        val response = dataSource.getPhotos(pageNumber, pageSize)
        emit(applyFavourites(response))
    }.flowOn(IO)

    override suspend fun search(
        text: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<PhotosResponse> = flow {
        val response = dataSource.search(text, pageNumber, pageSize)
        emit(applyFavourites(response))
    }.flowOn(IO)

    override suspend fun applyFavourites(responseDto: PhotosResponseDto): PhotosResponse {
        val response = mapper.map(responseDto)
        val favouritesPhotos =
            localDataSource.getPhotosByIds(response.photos.map { it.id }).associateBy { it.id }
        response.photos.forEach {
            it.isFavourite = favouritesPhotos[it.id] != null
        }
        return response
    }
}
