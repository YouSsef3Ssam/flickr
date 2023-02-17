package com.youssef.flickr.framework.presentation.features.photos

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.youssef.flickr.framework.presentation.callback.OnItemClickListener
import com.youssef.flickr.framework.presentation.diffCallback.PhotoDiffCallback
import com.youssef.flickr.framework.presentation.entities.Photo
import javax.inject.Inject

class PhotosAdapter @Inject constructor() :
    ListAdapter<Photo, PhotoHolder>(PhotoDiffCallback()) {
    private var onItemClickListener: OnItemClickListener<Photo>? = null
    fun listen(onItemClickListener: OnItemClickListener<Photo>) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
        PhotoHolder.from(parent, onItemClickListener)
}