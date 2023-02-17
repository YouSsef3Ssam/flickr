package com.youssef.flickr.framework.datasources.local.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youssef.flickr.framework.datasources.local.room.entities.LocalPhotoEntity
import com.youssef.flickr.framework.utils.Constants.LocalDataBase.Tables
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: LocalPhotoEntity): Long

    @Query("SELECT * FROM ${Tables.PHOTOS}")
    fun getPhotos(): Flow<List<LocalPhotoEntity>>

    @Query("SELECT * FROM ${Tables.PHOTOS} WHERE id IN (:ids)")
    fun getPhotosByIds(ids: List<String>): List<LocalPhotoEntity>

    @Delete
    suspend fun delete(vararg photo: LocalPhotoEntity): Int
}
