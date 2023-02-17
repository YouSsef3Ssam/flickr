package com.youssef.flickr.framework.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.framework.presentation.features.photoDetails.PhotoDetailsViewModel
import com.youssef.flickr.framework.utils.states.DataState
import com.youssef.flickr.utils.Mocks
import io.mockk.coEvery
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PhotoDetailsViewModel
    private val useCase: FavouritesUseCase = mockkClass(FavouritesUseCase::class)

    private val expectedAddToFavouriteSuccessResult = 1L
    private val expectedRemoveFromFavouriteSuccessResult = 1
    private val expectedDownloadPhotoSuccessResult = "path"
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `addToFavourite with first page then return First`() = runBlocking {
        coEvery { useCase.addToFavourite(any()) } answers {
            flow { emit(expectedAddToFavouriteSuccessResult) }
        }
        val stateHandle = SavedStateHandle()
        stateHandle["entity"] = Mocks.photo.copy(isFavourite = false)
        viewModel = PhotoDetailsViewModel(useCase, stateHandle)
        viewModel.didClickFavourite()
        assertTrue(viewModel.addToFavouriteDataState.value is DataState.Success)
        val actual = (viewModel.addToFavouriteDataState.value as DataState.Success).data
        assertEquals(expectedAddToFavouriteSuccessResult, actual)
    }

    @Test
    fun `addToFavourite with failure response then return error`() = runBlocking {
        coEvery { useCase.addToFavourite(any()) } answers { flow { throw expectedFailureResult } }
        val stateHandle = SavedStateHandle()
        stateHandle["entity"] = Mocks.photo.copy(isFavourite = false)
        viewModel = PhotoDetailsViewModel(useCase, stateHandle)
        viewModel.didClickFavourite()
        assertTrue(viewModel.addToFavouriteDataState.value is DataState.Failure)
        val actual = (viewModel.addToFavouriteDataState.value as DataState.Failure).throwable
        assertEquals(expectedFailureResult, actual)
    }

    @Test
    fun `removeFromFavourite with first page then return First`() = runBlocking {
        coEvery { useCase.removeFromFavourite(any()) } answers {
            flow { emit(expectedRemoveFromFavouriteSuccessResult) }
        }
        val stateHandle = SavedStateHandle()
        stateHandle["entity"] = Mocks.photo.copy(isFavourite = true)
        viewModel = PhotoDetailsViewModel(useCase, stateHandle)
        viewModel.didClickFavourite()
        assertTrue(viewModel.removeFromFavouriteDataState.value is DataState.Success)
        val actual = (viewModel.removeFromFavouriteDataState.value as DataState.Success).data
        assertEquals(expectedRemoveFromFavouriteSuccessResult, actual)
    }

    @Test
    fun `removeFromFavourite with failure response then return error`() = runBlocking {
        coEvery { useCase.removeFromFavourite(any()) } answers { flow { throw expectedFailureResult } }
        val stateHandle = SavedStateHandle()
        stateHandle["entity"] = Mocks.photo.copy(isFavourite = true)
        viewModel = PhotoDetailsViewModel(useCase, stateHandle)
        viewModel.didClickFavourite()
        assertTrue(viewModel.removeFromFavouriteDataState.value is DataState.Failure)
        val actual = (viewModel.removeFromFavouriteDataState.value as DataState.Failure).throwable
        assertEquals(expectedFailureResult, actual)
    }

    @Test
    fun `downloadPhoto with first page then return First`() = runBlocking {
        coEvery { useCase.downloadPhoto(any()) } answers {
            flow { emit(expectedDownloadPhotoSuccessResult) }
        }
        val stateHandle = SavedStateHandle()
        stateHandle["entity"] = Mocks.photo
        viewModel = PhotoDetailsViewModel(useCase, stateHandle)
        viewModel.downloadPhoto()
        assertTrue(viewModel.downloadPhotoDataState.value is DataState.Success)
        val actual = (viewModel.downloadPhotoDataState.value as DataState.Success).data
        assertEquals(expectedDownloadPhotoSuccessResult, actual)
    }

    @Test
    fun `downloadPhoto with failure response then return error`() = runBlocking {
        coEvery { useCase.downloadPhoto(any()) } answers { flow { throw expectedFailureResult } }
        val stateHandle = SavedStateHandle()
        stateHandle["entity"] = Mocks.photo
        viewModel = PhotoDetailsViewModel(useCase, stateHandle)
        viewModel.downloadPhoto()
        assertTrue(viewModel.downloadPhotoDataState.value is DataState.Failure)
        val actual = (viewModel.downloadPhotoDataState.value as DataState.Failure).throwable
        assertEquals(expectedFailureResult, actual)
    }
}
