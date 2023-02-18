package com.youssef.flickr.business.mappers

import com.youssef.flickr.business.entities.PhotoDto
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.utils.Mocks
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoMapperTest {
    private lateinit var mapper: EntityMapper<PhotoDto, Photo>

    @Before
    fun setUp() {
        mapper = PhotoMapper()
    }

    @Test
    fun `test map`() {
        val photoDto = Mocks.photoDto
        val response = mapper.map(photoDto)
        assertEquals(Photo::class.java, response.javaClass)
        assertEquals(photoDto.id, response.id)
        assertEquals(photoDto.title, response.title)
        assertEquals(
            "https://farm${photoDto.farm}.staticflickr.com/${photoDto.server}/${photoDto.id}_${photoDto.secret}.jpg",
            response.url
        )
        assertEquals(false, response.isFavourite)
    }
}
