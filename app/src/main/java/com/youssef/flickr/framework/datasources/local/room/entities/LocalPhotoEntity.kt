package com.youssef.flickr.framework.datasources.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youssef.flickr.framework.utils.Constants.LocalDataBase.Tables

@Entity(tableName = Tables.PHOTOS)
class LocalPhotoEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String
)
