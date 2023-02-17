package com.youssef.flickr.framework.datasources.local.mappers

import com.youssef.flickr.framework.datasources.local.room.entities.LocalPhotoEntity
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.utils.Mocks
import com.youssef.flickr.utils.Mocks.localPhoto
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalPhotoMapperTest {
    private lateinit var mapper: LocalEntityMapper<LocalPhotoEntity, Photo>

    @Before
    fun setUp() {
        mapper = LocalPhotoMapper()
    }

    @Test
    fun `test mapFrom`() {
        val localPhoto = Mocks.localPhoto
        val response = mapper.mapFrom(localPhoto)
        assertEquals(Photo::class.java, response.javaClass)
        assertEquals(localPhoto.id, response.id)
        assertEquals(localPhoto.title, response.title)
        assertEquals(localPhoto.url, response.url)
        assertEquals(true, response.isFavourite)
    }

    @Test
    fun `test mapTo`() {
        val photo = Mocks.photo
        val response = mapper.mapTo(photo)
        assertEquals(LocalPhotoEntity::class.java, response.javaClass)
        assertEquals(localPhoto.id, response.id)
        assertEquals(localPhoto.title, response.title)
        assertEquals(localPhoto.url, response.url)
    }

}