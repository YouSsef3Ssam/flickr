package com.youssef.flickr.framework.utils

import com.youssef.flickr.BuildConfig
import com.youssef.flickr.R

object Constants {
    object Network {
        const val BASE_URL: String = BuildConfig.HOST

        object EndPoints {
            private const val REST = "rest/"
            const val PHOTOS = REST
        }

        object Queries {
            const val API_KEY = "api_key"
            const val METHOD_KEY = "method"
            const val RECENT_PHOTOS = "flickr.photos.getRecent"
            const val SEARCH = "flickr.photos.search"
            const val FORMAT_KEY = "format"
            const val FORMAT_VALUE = "json"
            const val NO_JSON_CALLBACK_KEY = "nojsoncallback"
            const val NO_JSON_CALLBACK_VALUE = "1"
            const val PAGE_NUMBER = "page"
            const val PAGE_SIZE = "per_page"
            const val SEARCH_KEY = "text"
        }
    }

    object Pagination {
        const val PAGE_NUMBER = 1
        const val PAGE_SIZE = 20
    }

    object LocalDataBase {
        const val DATABASE_NAME = "flickr_database"

        object Tables {
            const val PHOTOS = "photos"
        }
    }

    const val debounceTime = 1000L
    val supportedBottomNavFragments = listOf(R.id.photosFragment, R.id.favouriteFragment)
}