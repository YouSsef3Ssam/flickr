package com.youssef.flickr.framework.datasources.local.mappers

import com.youssef.flickr.framework.datasources.local.room.entities.LocalPhotoEntity
import com.youssef.flickr.framework.presentation.entities.Photo
import javax.inject.Inject

class LocalPhotoMapper @Inject constructor() : LocalEntityMapper<LocalPhotoEntity, Photo> {

    override fun mapFrom(entity: LocalPhotoEntity): Photo = with(entity) {
        Photo(
            id = id,
            title = title,
            url = url,
            isFavourite = true
        )
    }

    override fun mapTo(response: Photo): LocalPhotoEntity = with(response) {
        LocalPhotoEntity(
            id = id,
            title = title,
            url = url
        )
    }
}