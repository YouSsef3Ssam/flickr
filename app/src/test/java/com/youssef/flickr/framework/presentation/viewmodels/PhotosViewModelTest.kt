package com.youssef.flickr.framework.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.youssef.flickr.business.usecases.abstraction.PhotosUseCase
import com.youssef.flickr.framework.presentation.features.photos.PhotosViewModel
import com.youssef.flickr.framework.utils.states.PaginationDataState
import com.youssef.flickr.utils.Mocks
import io.mockk.coEvery
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
class PhotosViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PhotosViewModel
    private val useCase: PhotosUseCase = mockkClass(PhotosUseCase::class)

    private val expectedGetPhotosSuccessResult = Mocks.photosResponse
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = PhotosViewModel(useCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test first page then return First`() = runBlocking {
        coEvery { useCase.getPhotos(any(), any()) } answers {
            flow { emit(expectedGetPhotosSuccessResult) }
        }
        delay(2000)
        assertTrue(viewModel.photosDataState.value is PaginationDataState.First)
        val actual = (viewModel.photosDataState.value as PaginationDataState.First).data
        assertEquals(expectedGetPhotosSuccessResult.photos, actual)
    }

    @Test
    fun `test next page then return Append`() = runBlocking {
        coEvery { useCase.getPhotos(any(), any()) } answers {
            flow { emit(expectedGetPhotosSuccessResult.copy(firstPage = false)) }
        }
        delay(2000)
        assertTrue(viewModel.photosDataState.value is PaginationDataState.Append)
        val actual = (viewModel.photosDataState.value as PaginationDataState.Append).data
        assertEquals(expectedGetPhotosSuccessResult.photos, actual)
    }

    @Test
    fun `test last page then return PaginationEnded`() = runBlocking {
        coEvery { useCase.getPhotos(any(), any()) } answers {
            flow { emit(expectedGetPhotosSuccessResult.copy(lastPage = true)) }
        }
        delay(2000)
        assertEquals(viewModel.photosDataState.value, PaginationDataState.PaginationEnded)
    }

    @Test
    fun `getImages with failure response then return error`() = runBlocking {
        coEvery { useCase.getPhotos(any(), any()) } answers { flow { throw expectedFailureResult } }
        viewModel = PhotosViewModel(useCase)
        delay(2000)
        assertTrue(viewModel.photosDataState.value is PaginationDataState.Failure)
        val actual = (viewModel.photosDataState.value as PaginationDataState.Failure).throwable
        assertEquals(expectedFailureResult, actual)
    }

    @Test
    fun `test loadNextPage with empty searchKey then call getPhotos`() = runBlocking {
        coEvery { useCase.getPhotos(any(), any()) } answers {
            flow { emit(expectedGetPhotosSuccessResult.copy(firstPage = false)) }
        }
        viewModel.loadNextPage(2)
        delay(2000)
        assertTrue(viewModel.photosDataState.value is PaginationDataState.Append)
        val actual = (viewModel.photosDataState.value as PaginationDataState.Append).data
        assertEquals(expectedGetPhotosSuccessResult.photos, actual)
    }

    @Test
    fun `test loadNextPage with non empty searchKey then call search`() = runBlocking {
        coEvery {
            useCase.search(any(), any(), any())
        } answers { flow { emit(expectedGetPhotosSuccessResult) } }
        viewModel.searchKey.value = "Hello"
        viewModel.loadNextPage(2)
        delay(2000)
        assertTrue(viewModel.photosDataState.value is PaginationDataState.First)
        val actual = (viewModel.photosDataState.value as PaginationDataState.First).data
        assertEquals(expectedGetPhotosSuccessResult.photos, actual)
    }

    @Test
    fun `search with failure response then return error`() = runBlocking {
        coEvery { useCase.getPhotos(any(), any()) } answers { flow { throw expectedFailureResult } }
        coEvery {
            useCase.search(any(), any(), any())
        } answers { flow { throw expectedFailureResult } }
        viewModel = PhotosViewModel(useCase)
        viewModel.searchKey.value = "Hello"
        delay(2000)
        assertTrue(viewModel.photosDataState.value is PaginationDataState.Failure)
        val actual = (viewModel.photosDataState.value as PaginationDataState.Failure).throwable
        assertEquals(expectedFailureResult, actual)
    }
}
