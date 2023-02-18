package com.youssef.flickr.framework.datasources.remote

import com.youssef.flickr.framework.datasources.remote.abstraction.PhotosDataSource
import com.youssef.flickr.framework.datasources.remote.impl.PhotosDataSourceImpl
import com.youssef.flickr.framework.datasources.remote.services.PhotosApi
import com.youssef.flickr.utils.Mocks
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotosDataSourceTest {
    private lateinit var dataSource: PhotosDataSource
    private val api = mockkClass(PhotosApi::class)
    private val apiResponse = Mocks.apiResponse

    private val expectedPhotosSuccessResult = apiResponse
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @Before
    fun setUp() {
        dataSource = PhotosDataSourceImpl(api)
    }

    @Test
    fun `getPhotos with success response then return success`() = runBlocking {
        coEvery { api.getPhotos(any(), any(), any()) } answers { expectedPhotosSuccessResult }
        val response = dataSource.getPhotos(1, 20)
        assertNotNull(response)
        assertEquals(expectedPhotosSuccessResult.photos, response)
    }

    @Test
    fun `getPhotos with failure response then return error`() = runBlocking {
        coEvery { api.getPhotos(any(), any(), any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.getPhotos(1, 20)
        } catch (e: RuntimeException) {
            response = e
        }

        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }

    @Test
    fun `search with success response then return success`() = runBlocking {
        coEvery { api.search(any(), any(), any(), any()) } answers { expectedPhotosSuccessResult }
        val response = dataSource.search("Dartmoor pony", 1, 20)
        assertNotNull(response)
        assertEquals(expectedPhotosSuccessResult.photos, response)
    }

    @Test
    fun `search with failure response then return error`() = runBlocking {
        coEvery { api.search(any(), any(), any(), any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.search("Dartmoor pony", 1, 20)
        } catch (e: RuntimeException) {
            response = e
        }

        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }
}
