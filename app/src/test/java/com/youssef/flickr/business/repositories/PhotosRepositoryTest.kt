package com.youssef.flickr.business.repositories

import com.youssef.flickr.business.mappers.PhotoMapper
import com.youssef.flickr.business.mappers.PhotoResponseMapper
import com.youssef.flickr.business.repositories.abstraction.PhotosRepository
import com.youssef.flickr.business.repositories.impl.PhotosRepositoryImpl
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.datasources.remote.abstraction.PhotosDataSource
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import com.youssef.flickr.utils.Mocks
import io.mockk.coEvery
import io.mockk.coVerify
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

    private val dataSource: PhotosDataSource = mockkClass(PhotosDataSource::class)
    private val localDataSource: LocalPhotosDataSource = mockkClass(LocalPhotosDataSource::class)
    private val mapper = PhotoResponseMapper(PhotoMapper())

    private val expectedGetPhotosSuccessResult = Mocks.apiResponse.photos
    private val expectedFailureResult = RuntimeException("Can't get the photos")

    @Before
    fun setUp() {
        repository = PhotosRepositoryImpl(dataSource, localDataSource, mapper)
    }

    @Test
    fun `getPhotos with success response then return success`() = runBlocking {
        val localPhotos = listOf(Mocks.photo)
        coEvery { dataSource.getPhotos(any(), any()) } returns expectedGetPhotosSuccessResult
        coEvery { localDataSource.getPhotosByIds(any()) } returns localPhotos
        val response = repository.getPhotos(1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        coVerify(exactly = 1) { localDataSource.getPhotosByIds(expectedGetPhotosSuccessResult.photo.map { it.id }) }
    }

    @Test
    fun `getPhotos with failure response then return error`() = runBlocking {
        coEvery { dataSource.getPhotos(any(), any()) } answers { throw expectedFailureResult }
        val response = repository.getPhotos(1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
        coVerify(exactly = 0) { localDataSource.getPhotosByIds(expectedGetPhotosSuccessResult.photo.map { it.id }) }
    }

    @Test
    fun `search with success response then return success`() = runBlocking {
        val localPhotos = listOf(Mocks.photo)
        coEvery { dataSource.search(any(), any(), any()) } returns expectedGetPhotosSuccessResult
        coEvery { localDataSource.getPhotosByIds(any()) } returns localPhotos
        val response = repository.search("Hello World", 1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null
        response
            .catch { error = it }
            .collect { success = it }

        assertNotNull(success)
        assertNull(error)
        coVerify(exactly = 1) { localDataSource.getPhotosByIds(expectedGetPhotosSuccessResult.photo.map { it.id }) }
    }

    @Test
    fun `search with failure response then return error`() = runBlocking {
        coEvery { dataSource.search(any(), any(), any()) } answers { throw expectedFailureResult }
        val response = repository.search("Hello World", 1, 20)

        var success: PhotosResponse? = null
        var error: Throwable? = null

        response
            .catch { error = it }
            .collect { success = it }

        assertNull(success)
        assertNotNull(error)
        assertEquals(expectedFailureResult.message, error!!.message)
        coVerify(exactly = 0) { localDataSource.getPhotosByIds(expectedGetPhotosSuccessResult.photo.map { it.id }) }
    }

    @Test
    fun `test applyFavourites`() = runBlocking {
        val responseDto = Mocks.photosResponseDto
        val photos = listOf(Mocks.photo)
        coEvery { localDataSource.getPhotosByIds(any()) } returns photos
        val response = repository.applyFavourites(responseDto)
        assertNotNull(response)
        assertEquals(PhotosResponse::class.java, response.javaClass)
        assertEquals(expectedGetPhotosSuccessResult.page == 1, response.firstPage)
        assertEquals(
            expectedGetPhotosSuccessResult.page == expectedGetPhotosSuccessResult.pages,
            response.lastPage
        )
        assertEquals(expectedGetPhotosSuccessResult.photo.first().id, response.photos.first().id)
        assertEquals(
            response.photos.first().id == photos.first().id,
            response.photos.first().isFavourite
        )
        coVerify(exactly = 1) { localDataSource.getPhotosByIds(expectedGetPhotosSuccessResult.photo.map { it.id }) }
    }
}