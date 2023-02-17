package com.youssef.flickr.business.entities.errors

data class ServerError(val stat: String, val code: Int, val message: String)
