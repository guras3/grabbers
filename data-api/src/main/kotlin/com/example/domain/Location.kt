package com.example.domain

data class Location(
        val country: String?,
        val locationType: String?,
        val locationName: String?,
        val polygon: List<GeoPoint>? = null,
        val exactCoordinates: GeoPoint? = null
) {
    init {
        if (polygon == null && exactCoordinates == null) {
            throw IllegalArgumentException("polygon == null && coordinates == null")
        }
    }
}
