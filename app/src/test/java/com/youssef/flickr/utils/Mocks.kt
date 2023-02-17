package com.youssef.flickr.utils

import com.youssef.flickr.business.entities.ApiResponse
import com.youssef.flickr.business.entities.PhotoDto
import com.youssef.flickr.business.entities.PhotosResponseDto
import com.youssef.flickr.framework.datasources.local.room.entities.LocalPhotoEntity
import com.youssef.flickr.framework.presentation.entities.Photo

object Mocks {
    private val photoDto = PhotoDto(
        id = "1",
        owner = "1232",
        secret = "123",
        server = "asd",
        farm = 2,
        title = "Hello World!"
    )

    private val photosResponseDto =
        PhotosResponseDto(page = 1, pages = 100, photo = listOf(photoDto))

    val apiResponse = ApiResponse(photos = photosResponseDto)

    val localPhoto = LocalPhotoEntity(id = "1", title = "Hello World!", url = "photoUrl")
    val photo = Photo(id = "1", title = "Hello World!", url = "photoUrl", isFavourite = true)

}