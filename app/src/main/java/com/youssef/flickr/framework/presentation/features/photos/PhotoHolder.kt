package com.youssef.flickr.framework.presentation.features.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youssef.flickr.databinding.ItemPhotoBinding
import com.youssef.flickr.framework.presentation.callback.OnItemClickListener
import com.youssef.flickr.framework.presentation.entities.Photo

class PhotoHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo) {
        binding.photo = photo
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener<Photo>?
        ): PhotoHolder {
            val binding =
                ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.listener = onItemClickListener
            return PhotoHolder(binding)
        }
    }
}
