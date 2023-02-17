package com.youssef.flickr.business.entities

class PhotosResponseDto(
    val page: Int,
    val pages: Int,
    val photo: List<PhotoDto>
)