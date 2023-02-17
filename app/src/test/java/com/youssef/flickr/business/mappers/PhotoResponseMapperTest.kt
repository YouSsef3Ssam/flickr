package com.youssef.flickr.business.mappers

import com.youssef.flickr.business.entities.PhotosResponseDto
import com.youssef.flickr.framework.presentation.entities.PhotosResponse
import com.youssef.flickr.utils.Mocks
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoResponseMapperTest {
    private lateinit var mapper: EntityMapper<PhotosResponseDto, PhotosResponse>

    @Before
    fun setUp() {
        mapper = PhotoResponseMapper(PhotoMapper())
    }

    @Test
    fun `test map`() {
        val photosResponseDto = Mocks.photosResponseDto
        val response = mapper.map(photosResponseDto)
        assertEquals(PhotosResponse::class.java, response.javaClass)
        assertEquals(photosResponseDto.page == 1, response.firstPage)
        assertEquals(photosResponseDto.page == photosResponseDto.pages, response.lastPage)
        assertEquals(photosResponseDto.photo.size, response.photos.size)
        assertEquals(photosResponseDto.photo.first().id, response.photos.first().id)
        assertEquals(photosResponseDto.photo.first().title, response.photos.first().title)
        assertEquals(false, response.photos.first().isFavourite)
    }
}
