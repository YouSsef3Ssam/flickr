package com.youssef.flickr.framework.presentation.features.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssef.flickr.business.usecases.abstraction.PhotosUseCase
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import com.youssef.flickr.framework.utils.Constants.Pagination
import com.youssef.flickr.framework.utils.ext.catchError
import com.youssef.flickr.framework.utils.ext.debounceFlow
import com.youssef.flickr.framework.utils.ext.launchIdling
import com.youssef.flickr.framework.utils.states.PaginationDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(private val useCase: PhotosUseCase) : ViewModel() {
    private val _photosDataState: MutableLiveData<PaginationDataState<List<Photo>>> =
        MutableLiveData()
    val photosDataState: LiveData<PaginationDataState<List<Photo>>> get() = _photosDataState

    var searchKey: MutableLiveData<String> = MutableLiveData("")

    init {
        listenOnSearchKey()
    }

    @OptIn(FlowPreview::class)
    private fun listenOnSearchKey() {
        searchKey.debounceFlow(viewModelScope) {
            requestPhotos(text = it, pageNumber = Pagination.PAGE_NUMBER)
        }
    }

    private fun requestPhotos(text: String, pageNumber: Int) {
        if (text.isEmpty()) {
            getImages(pageNumber = pageNumber)
        } else {
            search(pageNumber = pageNumber, text = text)
        }
    }

    private fun getImages(pageNumber: Int) {
        viewModelScope.launchIdling {
            useCase.getPhotos(pageNumber, Pagination.PAGE_SIZE)
                .handleResponse(pageNumber)
        }
    }

    private fun search(pageNumber: Int, text: String) {
        viewModelScope.launchIdling {
            useCase.search(text = text, pageNumber, Pagination.PAGE_SIZE)
                .handleResponse(pageNumber)
        }
    }

    fun loadNextPage(pageNumber: Int) {
        requestPhotos(text = searchKey.value ?: "", pageNumber = pageNumber)
    }

    private suspend fun Flow<PhotosResponse>.handleResponse(pageNumber: Int) {
        this.onStart {
            if (pageNumber == 1) {
                _photosDataState.value = PaginationDataState.FirstLoading
            } else {
                _photosDataState.value = PaginationDataState.PaginationLoading
            }
        }
            .catchError { _photosDataState.value = PaginationDataState.Failure(it) }
            .collect {
                when {
                    it.lastPage -> _photosDataState.value = PaginationDataState.PaginationEnded
                    it.firstPage ->
                        _photosDataState.value =
                            PaginationDataState.First(it.photos)
                    else -> _photosDataState.value = PaginationDataState.Append(it.photos)
                }
            }
    }
}
