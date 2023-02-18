package com.youssef.flickr.business.usecases

import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.business.usecases.abstraction.PhotosUseCase
import com.youssef.flickr.business.usecases.impl.PhotosUseCaseImpl
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import com.youssef.flickr.utils.Mocks
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotosUseCaseTest {

    private lateinit var useCase: PhotosUseCase

    private val repository: PhotosRepository = mockkClass(PhotosRepository::class)

    private val expectedGetPhotosSuccessResult = Mocks.photosResponse
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @Before
    fun setUp() {
        useCase = PhotosUseCaseImpl(repository)
    }

    @Test
    fun `getPhotos with success response then return success`() = runBlocking {
        coEvery { repository.getPhotos(any(), any()) } answers {
            flow { emit(expectedGetPhotosSuccessResult) }
        }
        val response = useCase.getPhotos(1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedGetPhotosSuccessResult.photos.size, success?.photos?.size)
    }

    @Test
    fun `getPhotos with failure response then return error`() = runBlocking {
        coEvery { repository.getPhotos(any(), any()) } answers {
            flow { throw expectedFailureResult }
        }
        val response = useCase.getPhotos(1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }

    @Test
    fun `search with success response then return success`() = runBlocking {
        coEvery { repository.search(any(), any(), any()) } answers {
            flow { emit(expectedGetPhotosSuccessResult) }
        }
        val response = useCase.search("Hello World!", 1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
    }

    @Test
    fun `search with failure response then return error`() = runBlocking {
        coEvery { repository.search(any(), any(), any()) } answers {
            flow { throw expectedFailureResult }
        }
        val response = useCase.search("Hello World!", 1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }
}
