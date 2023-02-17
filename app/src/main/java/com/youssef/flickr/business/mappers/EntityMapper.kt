package com.youssef.flickr.business.mappers

interface EntityMapper<Entity, Response> {

    fun map(entity: Entity): Response
}