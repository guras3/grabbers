package com.example.twittergrabber

import com.example.core.domain.*
import com.example.core.services.PersistentMessageQueue
import com.vaadin.spring.annotation.EnableVaadin
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import twitter4j.FilterQuery
import twitter4j.GeoLocation
import twitter4j.Status
import twitter4j.TwitterStreamFactory
import javax.annotation.PostConstruct

@SpringBootApplication(scanBasePackages = arrayOf(
        "com.example.core",
        "com.example.twittergrabber"
))
@EnableMongoRepositories(basePackages = arrayOf("com.example.core.repositories"))
@EnableVaadin

class TwitterGrabberApplication {

    private companion object : KLogging()

    @Autowired
    lateinit var twitterStreamFactory: TwitterStreamFactory
    @Autowired
    lateinit var persistentMessageQueue: PersistentMessageQueue

    @PostConstruct
    fun openStream() {
        val twitterStream = twitterStreamFactory.instance

        val filterQuery = FilterQuery(
                0,
                null,
                null,
                arrayOf(doubleArrayOf(22.284086, 35.970508),
                        doubleArrayOf(158.162992, 73.843899)),
                arrayOf("ru"))

        twitterStream.onException { ex -> logger.error(ex) { } }
                .onStatus { status -> persistentMessageQueue.offer(convertToMessage(status)) }
                .filter(filterQuery)

    }

    private fun convertToMessage(status: Status) = Message(
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

    private fun buildCoordinates(geoLocation: GeoLocation?): GeoJsonPoint? {
        return geoLocation?.toGeoJsonPoint()
    }

    private fun buildLocation(status: Status): Location {
        return when (status.place.boundingBoxType) {
            "Polygon" -> {
                val points = status.place.boundingBoxCoordinates[0].map { it.toGeoJsonPoint() }
                //first and last position must be the same
                val boundingBox = GeoJsonPolygon(points + points.first())
                Location(country = status.place.country,
                        locationType = status.place.placeType,
                        locationName = status.place.fullName,
                        boundingBox = boundingBox,
                        exactCoordinates = buildCoordinates(status.geoLocation)
                )
            }
            else -> {
                logger.error { "unknown location type in $status" }
                throw RuntimeException("unknown location type in ${status.place}")
            }
        }
    }

    private fun GeoLocation.toGeoJsonPoint(): GeoJsonPoint {
        // GeoJSON specifies a longitude then a latitude,
        return GeoJsonPoint(this.longitude, this.latitude)
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterGrabberApplication>(*args)
}
