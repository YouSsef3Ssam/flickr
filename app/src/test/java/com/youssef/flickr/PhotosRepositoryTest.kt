package com.youssef.flickr

import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.business.repositories.impl.PhotosRepositoryImpl
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotosRepositoryTest {

    private lateinit var repository: PhotosRepository

    private val dataSource: LocalPhotosDataSource = mockkClass(LocalPhotosDataSource::class)
    private val image = mockkClass(ImageModel::class)

    private val expectedGetImageSuccessResult = image
    private val expectedFailureResult = RuntimeException("Can't get the images")

    @Before
    fun setUp() {
        repository = PhotosRepositoryImpl(dataSource)
    }


    @Test
    fun `getImages with success response then return success`() = runBlocking {
        coEvery { dataSource.getImages() } returns image
        val response = repository.getImages()

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
        coEvery { dataSource.getImages() } answers { throw expectedFailureResult }
        val response = repository.getImages()

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