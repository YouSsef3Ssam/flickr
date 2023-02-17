package com.youssef.flickr

import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.business.usecases.abstraction.PhotosUseCase
import com.youssef.flickr.business.usecases.impl.PhotosUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotosUseCaseTest {

    private val repository: PhotosRepository = mockkClass(PhotosRepository::class)
    private lateinit var useCase: PhotosUseCase
    private val image = mockkClass(ImageModel::class)

    private val expectedGetImageSuccessResult = image
    private val expectedFailureResult = RuntimeException("Can't get the images")

    @Before
    fun setUp() {
        useCase = PhotosUseCaseImpl(repository)
    }

    @Test
    fun `getImages with success response then return success`() = runBlocking {
        coEvery { repository.getImages() } answers { flow { emit(image) } }
        val response = useCase.getImages()

        var success: ImageModel? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedGetImageSuccessResult, success)
    }

    @Test
    fun `getImages with failure response then return error`() = runBlocking {
        coEvery { repository.getImages() } answers { flow { throw expectedFailureResult } }
        val response = useCase.getImages()

        var success: ImageModel? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }

}