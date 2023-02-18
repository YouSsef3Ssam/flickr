package com.youssef.flickr.business.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.youssef.flickr.R
import com.youssef.flickr.business.entities.errors.RemoveFavouriteException
import com.youssef.flickr.business.repositories.abstraction.FavouritesRepository
import com.youssef.flickr.framework.datasources.local.abstraction.LocalPhotosDataSource
import com.youssef.flickr.framework.presentation.entities.Photo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class FavouritesRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dataSource: LocalPhotosDataSource
) : FavouritesRepository {

    override suspend fun getFavouritePhotos(): Flow<List<Photo>> =
        dataSource.getPhotos().flowOn(IO)

    override suspend fun addToFavourite(photo: Photo): Flow<Long> = flow {
        emit(dataSource.addToFavourite(photo))
    }.flowOn(IO)

    override suspend fun removeFromFavourite(photo: Photo): Flow<Int> = flow {
        val result = dataSource.removeFromFavourite(photo)
        if (result == 0) throw RemoveFavouriteException(context.getString(R.string.action_failure_message))
        emit(result)
    }.flowOn(IO)

    override suspend fun downloadPhoto(url: String): Flow<String> = flow<String> {
        val imageLoader = context.imageLoader
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val result = (imageLoader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        val file = saveBitmapToStorage(bitmap)
        emit(file.absolutePath)
    }.flowOn(IO)

    private fun saveBitmapToStorage(bitmap: Bitmap): File {
        val fileName: String = System.currentTimeMillis().toString()
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(fileName, ".jpg", storageDir)
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
        return file
    }
}
