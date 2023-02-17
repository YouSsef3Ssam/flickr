package com.youssef.flickr

import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.datasources.local.impl.LocalPhotosDataSourceImpl
import com.youssef.flickr.framework.datasources.remote.services.PhotosApi
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
class LocalPhotosDataSourceTest {
    private lateinit var dataSource: LocalPhotosDataSource
    private val api = mockkClass(PhotosApi::class)
    private val image = mockkClass(ImageModel::class)

    private val expectedGetImageSuccessResult = image
    private val expectedFailureResult = RuntimeException("Can't get the images")

    @Before
    fun setUp() {
        dataSource = LocalPhotosDataSourceImpl(api)
    }

    @Test
    fun `getImages with success response then return success`() = runBlocking {
        coEvery { api.getImages(any()) } answers { expectedGetImageSuccessResult }
        val response = dataSource.getImages()

        assertNotNull(response)
        assertEquals(expectedGetImageSuccessResult, response)
    }

    @Test
    fun `getImages with failure response then return error`() = runBlocking {
        coEvery { api.getImages(any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.getImages()
        } catch (e: RuntimeException) {
            response = e
        }

        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }


}