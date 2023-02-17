package com.youssef.flickr.business.usecases

import com.youssef.flickr.business.repositories.abstraction.FavouritesRepository
import com.youssef.flickr.business.usecases.abstraction.FavouritesUseCase
import com.youssef.flickr.business.usecases.impl.FavouritesUseCaseImpl
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
class FavouritesUseCaseTest {

    private lateinit var useCase: FavouritesUseCase

    private val repository: FavouritesRepository = mockkClass(FavouritesRepository::class)

    private val expectedAddToFavouriteSuccessResult = 1L
    private val expectedRemoveFromFavouriteSuccessResult = 1
    private val expectedDownloadPhotoSuccessResult = "path"
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @Before
    fun setUp() {
        useCase = FavouritesUseCaseImpl(repository)
    }

    @Test
    fun `addToFavourite with success response then return success`() = runBlocking {
        coEvery { repository.addToFavourite(any()) } answers {
            flow { emit(expectedAddToFavouriteSuccessResult) }
        }
        val response = useCase.addToFavourite(Mocks.photo)

        var success: Long? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedAddToFavouriteSuccessResult, success)
    }

    @Test
    fun `addToFavourite with failure response then return error`() = runBlocking {
        coEvery { repository.addToFavourite(any()) } answers {
            flow { throw expectedFailureResult }
        }
        val response = useCase.addToFavourite(Mocks.photo)

        var success: Long? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }

    @Test
    fun `removeFromFavourite with success response then return success`() = runBlocking {
        coEvery { repository.removeFromFavourite(any()) } answers {
            flow { emit(expectedRemoveFromFavouriteSuccessResult) }
        }
        val response = useCase.removeFromFavourite(Mocks.photo)

        var success: Int? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedRemoveFromFavouriteSuccessResult, success)
    }

    @Test
    fun `removeFromFavourite with failure response then return error`() = runBlocking {
        coEvery { repository.removeFromFavourite(any()) } answers {
            flow { throw expectedFailureResult }
        }
        val response = useCase.removeFromFavourite(Mocks.photo)

        var success: Int? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }

    @Test
    fun `downloadPhoto with success response then return success`() = runBlocking {
        coEvery { repository.downloadPhoto(any()) } answers {
            flow { emit(expectedDownloadPhotoSuccessResult) }
        }
        val response = useCase.downloadPhoto("url")

        var success: String? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedDownloadPhotoSuccessResult, success)
    }

    @Test
    fun `downloadPhoto with failure response then return error`() = runBlocking {
        coEvery { repository.downloadPhoto(any()) } answers {
            flow { throw expectedFailureResult }
        }
        val response = useCase.downloadPhoto("url")

        var success: String? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }
}
