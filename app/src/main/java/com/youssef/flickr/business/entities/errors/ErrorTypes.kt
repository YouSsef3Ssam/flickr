package com.youssef.flickr.business.entities.errors

sealed class ErrorTypes {
    data class HttpError(val throwable: Throwable) : ErrorTypes()
    data class UnKnown(val throwable: Throwable) : ErrorTypes()
    object NetworkError : ErrorTypes()
    object TimeOut : ErrorTypes()
}
