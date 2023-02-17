package com.youssef.flickr.framework.utils

interface EntityMapper<Entity, Response> {

    fun map(entity: Entity): Response
}