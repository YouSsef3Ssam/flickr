package com.youssef.flickr.framework.presentation.features.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.framework.utils.ext.catchError
import com.youssef.flickr.framework.utils.ext.launchIdling
import com.youssef.flickr.framework.utils.states.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val useCase: FavouritesUseCase) :
    ViewModel() {

    private val _favouritePhotosDataState: MutableLiveData<DataState<List<Photo>>> =
        MutableLiveData()
    val favouritePhotosDataState: LiveData<DataState<List<Photo>>> get() = _favouritePhotosDataState

    init {
        getFavouritePhotos()
    }

    private fun getFavouritePhotos() {
        viewModelScope.launchIdling {
            useCase.getFavouritePhotos()
                .onStart { _favouritePhotosDataState.value = DataState.Loading }
                .catchError { _favouritePhotosDataState.value = DataState.Failure(it) }
                .collect { _favouritePhotosDataState.value = DataState.Success(it) }
        }
    }

    fun refresh() {
        getFavouritePhotos()
    }
}
