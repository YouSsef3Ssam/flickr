package com.youssef.flickr.framework.presentation.entities

class PhotosResponse(
    val firstPage: Boolean,
    val lastPage: Boolean,
    val photos: List<Photo>
)