package com.youssef.flickr.business.entities

class PhotoDto(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
)