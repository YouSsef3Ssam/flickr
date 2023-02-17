package com.youssef.flickr.framework.presentation.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val title: String,
    val url: String,
    var isFavourite: Boolean
) : Parcelable
