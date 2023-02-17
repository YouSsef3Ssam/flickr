package com.youssef.flickr.framework.datasources.local

import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.datasources.local.impl.LocalPhotosDataSourceImpl
import com.youssef.flickr.framework.datasources.local.mappers.LocalPhotoMapper
import com.youssef.flickr.framework.datasources.local.room.daos.PhotosDao
import com.youssef.flickr.framework.presentation.entities.Photo
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
class LocalPhotosDataSourceTest {
    private lateinit var dataSource: LocalPhotosDataSource
    private val dao = mockkClass(PhotosDao::class)
    private val mapper = LocalPhotoMapper()

    private val expectedPhotosSuccessResult = listOf(Mocks.localPhoto)
    private val expectedPhotosByIdsSuccessResult = listOf(Mocks.localPhoto)
    private val expectedInsertPhotosSuccessResult = 1L
    private val expectedRemovePhotosSuccessResult = 1
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @Before
    fun setUp() {
        dataSource = LocalPhotosDataSourceImpl(dao, mapper)
    }

    @Test
    fun `getPhotos with success response then return success`() = runBlocking {
        coEvery { dao.getPhotos() } answers { flow { emit(expectedPhotosSuccessResult) } }
        val response = dataSource.getPhotos()

        var success: List<Photo>? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        assertEquals(expectedPhotosSuccessResult.size, success?.size)
        assertEquals(expectedPhotosSuccessResult.first().id, success?.first()?.id)
        assertEquals(expectedPhotosSuccessResult.first().url, success?.first()?.url)
        assertEquals(true, success?.first()?.isFavourite)
    }

    @Test
    fun `getPhotos with failure response then return error`() = runBlocking {
        coEvery { dao.getPhotos() } answers { flow { throw expectedFailureResult } }
        val response = dataSource.getPhotos()

        var success: List<Photo>? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(error)
        assertNull(success)
        assertEquals(expectedFailureResult, error)
        assertEquals(expectedFailureResult.message, error?.message)
    }

    @Test
    fun `getPhotosByIds with success response then return success`() = runBlocking {
        coEvery { dao.getPhotosByIds(any()) } answers { expectedPhotosByIdsSuccessResult }
        val response = dataSource.getPhotosByIds(listOf("1", "2"))
        assertNotNull(response)
        assertEquals(expectedPhotosByIdsSuccessResult.size, response.size)
        assertEquals(expectedPhotosByIdsSuccessResult.first().id, response.first().id)
        assertEquals(expectedPhotosByIdsSuccessResult.first().title, response.first().title)
        assertEquals(expectedPhotosByIdsSuccessResult.first().url, response.first().url)
    }

    @Test
    fun `getPhotosByIds with failure response then return error`() = runBlocking {
        coEvery { dao.getPhotosByIds(any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.getPhotosByIds(listOf("1", "2"))
        } catch (e: RuntimeException) {
            response = e
        }

        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }

    @Test
    fun `addToFavourite with success response then return success`() = runBlocking {
        coEvery { dao.insertPhoto(any()) } answers { expectedInsertPhotosSuccessResult }
        val response = dataSource.addToFavourite(Mocks.photo)
        assertNotNull(response)
        assertEquals(expectedInsertPhotosSuccessResult, response)
    }

    @Test
    fun `addToFavourite with failure response then return error`() = runBlocking {
        coEvery { dao.insertPhoto(any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.addToFavourite(Mocks.photo)
        } catch (e: RuntimeException) {
            response = e
        }
        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }

    @Test
    fun `removeFromFavourite with success response then return success`() = runBlocking {
        coEvery { dao.delete(any()) } answers { expectedRemovePhotosSuccessResult }
        val response = dataSource.removeFromFavourite(Mocks.photo)
        assertNotNull(response)
        assertEquals(expectedRemovePhotosSuccessResult, response)
    }

    @Test
    fun `removeFromFavourite with failure response then return error`() = runBlocking {
        coEvery { dao.delete(any()) } throws expectedFailureResult
        var response: RuntimeException? = null
        try {
            dataSource.removeFromFavourite(Mocks.photo)
        } catch (e: RuntimeException) {
            response = e
        }
        assertNotNull(response)
        assertEquals(expectedFailureResult, response)
        assertEquals(expectedFailureResult.message, response?.message)
    }
}
