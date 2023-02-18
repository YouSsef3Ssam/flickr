package com.youssef.flickr.framework.presentation.entities

data class PhotosResponse(
    val firstPage: Boolean,
    val lastPage: Boolean,
    val photos: List<Photo>
)
