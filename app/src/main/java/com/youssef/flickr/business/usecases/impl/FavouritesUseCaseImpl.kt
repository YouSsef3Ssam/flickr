package com.youssef.flickr.business.usecases.impl

import com.youssef.flickr.business.repositories.abstraction.FavouritesRepository
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.framework.presentation.entities.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesUseCaseImpl @Inject constructor(private val repository: FavouritesRepository) :
    FavouritesUseCase {

    override suspend fun getFavouritePhotos(): Flow<List<Photo>> = repository.getFavouritePhotos()

    override suspend fun addToFavourite(photo: Photo): Flow<Long> = repository.addToFavourite(photo)

    override suspend fun removeFromFavourite(photo: Photo): Flow<Int> =
        repository.removeFromFavourite(photo)

    override suspend fun downloadPhoto(url: String): Flow<String> = repository.downloadPhoto(url)
}