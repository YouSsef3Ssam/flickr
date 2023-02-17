package com.youssef.flickr.framework.presentation.features.photoDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.framework.utils.SingleLiveEvent
import com.youssef.flickr.framework.utils.ext.catchError
import com.youssef.flickr.framework.utils.ext.launchIdling
import com.youssef.flickr.framework.utils.states.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val useCase: FavouritesUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val photo: Photo = stateHandle.get<Photo>("entity")!!

    private val _addToFavouriteDataState: SingleLiveEvent<DataState<Long>> = SingleLiveEvent()
    val addToFavouriteDataState: LiveData<DataState<Long>> get() = _addToFavouriteDataState

    private val _removeFromFavouriteDataState: SingleLiveEvent<DataState<Int>> = SingleLiveEvent()
    val removeFromFavouriteDataState: LiveData<DataState<Int>> get() = _removeFromFavouriteDataState

    private val _downloadPhotoDataState: SingleLiveEvent<DataState<String>> = SingleLiveEvent()
    val downloadPhotoDataState: LiveData<DataState<String>> get() = _downloadPhotoDataState

    fun didClickFavourite() {
        if (photo.isFavourite) {
            removeFromFavourite()
        } else {
            addToFavourite()
        }
    }

    private fun addToFavourite() {
        viewModelScope.launchIdling {
            useCase.addToFavourite(photo = photo)
                .onStart { _addToFavouriteDataState.value = DataState.Loading }
                .catchError { _addToFavouriteDataState.value = DataState.Failure(it) }
                .collect {
                    photo.isFavourite = true
                    _addToFavouriteDataState.value = DataState.Success(it)
                }
        }
    }

    private fun removeFromFavourite() {
        viewModelScope.launchIdling {
            useCase.removeFromFavourite(photo = photo)
                .onStart { _removeFromFavouriteDataState.value = DataState.Loading }
                .catchError { _removeFromFavouriteDataState.value = DataState.Failure(it) }
                .collect {
                    photo.isFavourite = false
                    _removeFromFavouriteDataState.value = DataState.Success(it)
                }
        }
    }

    fun downloadPhoto() {
        download(photo.url)
    }

    private fun download(url: String) {
        viewModelScope.launchIdling {
            useCase.downloadPhoto(url = url)
                .onStart { _downloadPhotoDataState.value = DataState.Loading }
                .catchError { _downloadPhotoDataState.value = DataState.Failure(it) }
                .collect { _downloadPhotoDataState.value = DataState.Success(it) }
        }
    }
}
