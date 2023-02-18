package com.youssef.flickr.business.mappers

import com.youssef.flickr.business.entities.PhotosResponseDto
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import javax.inject.Inject

class PhotoResponseMapper @Inject constructor(private val photoMapper: PhotoMapper) :
    EntityMapper<PhotosResponseDto, PhotosResponse> {

    override fun map(entity: PhotosResponseDto): PhotosResponse = with(entity) {
        PhotosResponse(
            firstPage = page == 1,
            lastPage = page == pages,
            photos = photo.map { photoMapper.map(it) }
        )
    }
}
