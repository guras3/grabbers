package com.example.datasaver.model

import com.example.domain.GeoPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed


internal data class Location(
        val country: String?,
        val locationType: String?,
        val locationName: String?,
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        val polygon: GeoJsonPolygon? = null,
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        val exactCoordinates: GeoJsonPoint? = null
) {
    init {
        if (polygon == null && exactCoordinates == null) {
            throw IllegalArgumentException("polygon == null && coordinates == null")
        }
    }

    companion object {
        fun fromExternal(external: com.example.domain.Location): Location {
            return Location(
                    country = external.country,
                    locationType = external.locationType,
                    locationName = external.locationName,
                    polygon = external.polygon?.let { toGeoJsonPolygon(it) },
                    exactCoordinates = external.exactCoordinates?.let { toGeoJsonPoint(it) }
            )
        }

        private fun toGeoJsonPolygon(coordinates: List<GeoPoint>): GeoJsonPolygon {
            val points = coordinates.map { toGeoJsonPoint(it) }
            //first and last position must be the same
            return if (points.first() == points.last()) GeoJsonPolygon(points)
            else GeoJsonPolygon(points + points.first())
        }

        private fun toGeoJsonPoint(external: GeoPoint): GeoJsonPoint {
            // GeoJSON specifies a longitude then a latitude,
            return GeoJsonPoint(external.longitude, external.latitude)
        }
    }
}