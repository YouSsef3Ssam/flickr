package com.youssef.flickr.framework.datasources.local.room


import androidx.room.Database
import androidx.room.RoomDatabase
import com.youssef.flickr.framework.datasources.local.room.daos.PhotosDao
import com.youssef.flickr.framework.datasources.local.room.entities.LocalPhotoEntity

@Database(
    entities = [LocalPhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FlickrDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotosDao
}