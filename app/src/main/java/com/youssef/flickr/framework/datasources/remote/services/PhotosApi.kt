package com.youssef.flickr.framework.datasources.remote.services

import com.youssef.flickr.business.entities.ApiResponse
import com.youssef.flickr.framework.utils.Constants.Network.EndPoints
import com.youssef.flickr.framework.utils.Constants.Network.Queries
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET(EndPoints.PHOTOS)
    suspend fun getPhotos(
        @Query(Queries.METHOD_KEY) method: String = Queries.RECENT_PHOTOS,
        @Query(Queries.PAGE_NUMBER) pageNumber: Int,
        @Query(Queries.PAGE_SIZE) pageSize: Int
    ): ApiResponse

    @GET(EndPoints.PHOTOS)
    suspend fun search(
        @Query(Queries.METHOD_KEY) method: String = Queries.SEARCH,
        @Query(Queries.PAGE_NUMBER) pageNumber: Int,
        @Query(Queries.PAGE_SIZE) pageSize: Int,
        @Query(Queries.SEARCH_KEY) text: String
    ): ApiResponse
}