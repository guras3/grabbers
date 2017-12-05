package com.example.twittergrabber

import com.example.domain.*
import twitter4j.GeoLocation
import twitter4j.Status

fun convertToMessage(status: Status) = Message(
        text = status.text,
        messageType = MessageTypeEnum.POST,
        origin = Origin(
                service = ServiceEnum.TWITTER,
                externalMessageId = status.id.toString(),
                creationDate = status.createdAt,
                author = Author(
                        externalAuthorId = status.user.id.toString(),
                        name = status.user.name,
                        gender = GenderEnum.UNDEFINED
                )
        ),
        location = buildLocation(status)
)

private fun buildLocation(status: Status): Location? {
    if (status.place == null && status.geoLocation == null) return null

    val points = if (status.place?.boundingBoxType == "Polygon") {
        status.place.boundingBoxCoordinates[0].map { it.toGeoJsonPoint() }
    } else null

    val exactCoordinates = status.geoLocation?.toGeoJsonPoint()

    return Location(
            country = status.place.country,
            locationType = status.place.placeType,
            locationName = status.place.fullName,
            polygon = points,
            exactCoordinates = exactCoordinates
    )
}

private fun GeoLocation.toGeoJsonPoint() = GeoPoint(longitude = this.longitude, latitude = this.latitude)