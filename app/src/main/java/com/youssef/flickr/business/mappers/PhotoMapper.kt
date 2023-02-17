package com.youssef.flickr.business.mappers

import com.youssef.flickr.business.entities.PhotoDto
import com.youssef.flickr.framework.presentation.entities.Photo
import javax.inject.Inject

class PhotoMapper @Inject constructor() : EntityMapper<PhotoDto, Photo> {

    override fun map(entity: PhotoDto): Photo = with(entity) {
        Photo(
            id = id,
            title = title,
            url = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg",
            isFavourite = false
        )
    }
}
