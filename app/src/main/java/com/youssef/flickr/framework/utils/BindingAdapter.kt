package com.youssef.flickr.framework.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.youssef.flickr.R

/**
 * [loadImage] used to load image/svg into an image view using glide
 * without caching from glide but integrate okhttp client and use its caching
 * mechanism internally
 */
@BindingAdapter(value = ["app:imageUrl", "app:placeholder"], requireAll = false)
fun ImageView.loadImage(imageUrl: String?, @DrawableRes placeholder: Int?) {
    this.load(imageUrl) {
        placeholder(placeholder ?: R.drawable.ic_image_placeholder)
        fallback(R.drawable.image_not_found)
        error(R.drawable.image_not_found)
        diskCachePolicy(CachePolicy.ENABLED)
        memoryCachePolicy(CachePolicy.ENABLED)
    }
}