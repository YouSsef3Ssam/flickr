package com.youssef.flickr.business.repositories

import android.content.Context
import com.youssef.flickr.business.repositories.abstraction.FavouritesRepository
import com.youssef.flickr.business.repositories.impl.FavouritesRepositoryImpl
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.utils.Mocks
import io.mockk.MockKAnnotations
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
class FavouritesRepositoryTest {

    private lateinit var repository: FavouritesRepository

    private val context: Context = mockkClass(Context::class)
    private val dataSource: LocalPhotosDataSource = mockkClass(LocalPhotosDataSource::class)

    private val expectedGetPhotosSuccessResult = listOf(Mocks.photo)
    private val expectedInsertPhotoSuccessResult = 1L
    private val expectedDeletePhotoSuccessResult = 1
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @Before
    fun setUp() {
        repository = FavouritesRepositoryImpl(context, dataSource)
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPhotos with success response then return success`() = runBlocking {
        coEvery { dataSource.getPhotos() } answers { flow { emit(expectedGetPhotosSuccessResult) } }
        val response = repository.getFavouritePhotos()

        var success: List<Photo>? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedGetPhotosSuccessResult.size, success?.size)
    }

    @Test
    fun `getPhotos with failure response then return error`() = runBlocking {
        coEvery { dataSource.getPhotos() } answers { flow { throw expectedFailureResult } }
        val response = repository.getFavouritePhotos()

        var success: List<Photo>? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }

    @Test
    fun `addToFavourite with success response then return success`() = runBlocking {
        coEvery { dataSource.addToFavourite(any()) } returns expectedInsertPhotoSuccessResult
        val response = repository.addToFavourite(Mocks.photo)

        var success: Long? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedInsertPhotoSuccessResult, success)
    }

    @Test
    fun `addToFavourite with failure response then return error`() = runBlocking {
        coEvery { dataSource.addToFavourite(any()) } throws expectedFailureResult
        val response = repository.addToFavourite(Mocks.photo)

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
        coEvery { dataSource.removeFromFavourite(any()) } returns expectedDeletePhotoSuccessResult
        val response = repository.removeFromFavourite(Mocks.photo)

        var success: Int? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedDeletePhotoSuccessResult, success)
    }

    @Test
    fun `removeFromFavourite with failure response then return error`() = runBlocking {
        coEvery { dataSource.removeFromFavourite(any()) } throws expectedFailureResult
        val response = repository.removeFromFavourite(Mocks.photo)

        var success: Int? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
    }
}
