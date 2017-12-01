package com.example.domain


data class Location(
        val country: String?,
        val locationType: String?,
        val locationName: String?,
        val boundingBox: List<GeoPoint>? = null,
        val exactCoordinates: GeoPoint? = null
) {
    init {
        if (boundingBox == null && exactCoordinates == null) {
            throw IllegalArgumentException("boundingBox == null && coordinates == null")
        }
    }
}

