package com.example.domain

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed


data class Location(
        val country: String?,
        val locationType: String?,
        val locationName: String?,
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        val boundingBox: GeoJsonPolygon? = null,
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        val exactCoordinates: GeoJsonPoint? = null
) {
    init {
        if (boundingBox == null && exactCoordinates == null) {
            throw IllegalArgumentException("boundingBox == null && coordinates == null")
        }
    }
}