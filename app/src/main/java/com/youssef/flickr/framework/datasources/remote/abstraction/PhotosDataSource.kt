package com.youssef.flickr.framework.datasources.remote.abstraction

import com.youssef.flickr.business.entities.PhotosResponseDto

interface PhotosDataSource {
    suspend fun getPhotos(pageNumber: Int, pageSize: Int): PhotosResponseDto
    suspend fun search(text: String, pageNumber: Int, pageSize: Int): PhotosResponseDto
}