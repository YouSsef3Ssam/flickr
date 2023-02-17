package com.youssef.flickr.framework.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.framework.presentation.features.favourite.FavouritesViewModel
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
class FavouritesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavouritesViewModel
    private val useCase: FavouritesUseCase = mockkClass(FavouritesUseCase::class)

    private val expectedGetPhotosSuccessResult = listOf(Mocks.photo)
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
    fun `getFavouritePhotos with first page then return First`() = runBlocking {
        coEvery { useCase.getFavouritePhotos() } answers {
            flow { emit(expectedGetPhotosSuccessResult) }
        }
        viewModel = FavouritesViewModel(useCase)
        assertTrue(viewModel.favouritePhotosDataState.value is DataState.Success)
        val actual = (viewModel.favouritePhotosDataState.value as DataState.Success).data
        assertEquals(expectedGetPhotosSuccessResult, actual)
    }

    @Test
    fun `getFavouritePhotos with failure response then return error`() = runBlocking {
        coEvery { useCase.getFavouritePhotos() } answers { flow { throw expectedFailureResult } }
        viewModel = FavouritesViewModel(useCase)
        assertTrue(viewModel.favouritePhotosDataState.value is DataState.Failure)
        val actual = (viewModel.favouritePhotosDataState.value as DataState.Failure).throwable
        assertEquals(expectedFailureResult, actual)
    }
}
