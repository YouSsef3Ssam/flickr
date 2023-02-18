package com.youssef.flickr.framework.datasources.remote.impl

import com.youssef.flickr.business.entities.PhotosResponseDto
import com.youssef.flickr.framework.datasources.remote.abstraction.PhotosDataSource
import com.youssef.flickr.framework.datasources.remote.services.PhotosApi
import javax.inject.Inject

class PhotosDataSourceImpl @Inject constructor(private val api: PhotosApi) :
    PhotosDataSource {

    override suspend fun getPhotos(pageNumber: Int, pageSize: Int): PhotosResponseDto =
        api.getPhotos(pageNumber = pageNumber, pageSize = pageSize).photos

    override suspend fun search(text: String, pageNumber: Int, pageSize: Int): PhotosResponseDto =
        api.search(text = text, pageNumber = pageNumber, pageSize = pageSize).photos
}
