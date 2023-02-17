package com.youssef.flickr.framework.datasources.local.mappers

interface LocalEntityMapper<Entity, Response> {

    fun mapFrom(entity: Entity): Response
    fun mapTo(response: Response): Entity
}
