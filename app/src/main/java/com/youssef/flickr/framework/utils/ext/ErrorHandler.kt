package com.youssef.flickr.framework.utils.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youssef.flickr.business.entities.errors.ErrorMessage
import com.youssef.flickr.business.entities.errors.ErrorTypes
import com.youssef.flickr.business.entities.errors.ErrorTypes.*
import com.youssef.flickr.business.entities.errors.ServerError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.getType(): ErrorTypes =
    when (this) {
        is ConnectException, is UnknownHostException -> NetworkError
        is HttpException -> HttpError(this)
        is TimeoutException -> TimeOut
        else -> UnKnown(this)
    }

fun ErrorTypes.getMessage(): ErrorMessage =
    when (this) {
        is NetworkError -> ErrorMessage(type = NetworkError)
        is TimeOut -> ErrorMessage(type = TimeOut)
        is HttpError -> getHttpError(throwable as HttpException)
        is UnKnown -> ErrorMessage(text = throwable.message, type = UnKnown(throwable))
    }

private fun getHttpError(throwable: HttpException): ErrorMessage {
    var message: String? = throwable.message()
    val gson = Gson()
    val type = object : TypeToken<ServerError>() {}.type
    throwable.response()?.errorBody()?.let {
        message = try {
            gson.fromJson<ServerError>(it.string(), type)?.message
        } catch (e: Exception) {
            null
        }
    }
    return ErrorMessage(message, HttpError(throwable))
}