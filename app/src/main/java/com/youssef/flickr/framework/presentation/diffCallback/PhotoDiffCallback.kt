package com.youssef.flickr.framework.presentation.diffCallback

import androidx.recyclerview.widget.DiffUtil
import com.youssef.flickr.framework.presentation.entities.Photo

class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
        oldItem == newItem
}
