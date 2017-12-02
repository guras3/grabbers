package com.example.twittergrabber

import com.example.domain.*
import com.example.kafka.EnableDataKafkaProducers
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.core.KafkaTemplate
import twitter4j.FilterQuery
import twitter4j.GeoLocation
import twitter4j.Status
import twitter4j.TwitterStreamFactory
import javax.annotation.PostConstruct

fun main(args: Array<String>) {
    runApplication<TwitterGrabberApplication>(*args)
}

@SpringBootApplication
@EnableDataKafkaProducers
class TwitterGrabberApplication {

    private companion object : KLogging()

    @Autowired
    lateinit var twitterStreamFactory: TwitterStreamFactory
    @Autowired
    lateinit var kafka: KafkaTemplate<String, Message>

    @PostConstruct
    fun init() {
        Thread { openTwitterStream() }.start()
    }

    private fun openTwitterStream() {
        val twitterStream = twitterStreamFactory.instance

        val filterQuery = FilterQuery(
                0,
                null,
                null,
                arrayOf(doubleArrayOf(22.284086, 35.970508),
                        doubleArrayOf(158.162992, 73.843899)),
                arrayOf("ru"))

        twitterStream.onException { ex -> logger.error(ex) { } }
                .onStatus { status ->
                    val message = convertToMessage(status)
                    logger.info { "sending message with id [${message.id}]" }
                    kafka.send("messages", message.id, message)
                }
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

    private fun buildLocation(status: Status): Location {
        return when (status.place.boundingBoxType) {
            "Polygon" -> {
                val points = status.place.boundingBoxCoordinates[0].map { it.toGeoJsonPoint() }
                val exactCoordinates = status.geoLocation?.let { it.toGeoJsonPoint() }
                Location(country = status.place.country,
                        locationType = status.place.placeType,
                        locationName = status.place.fullName,
                        polygon = points,
                        exactCoordinates = exactCoordinates
                )
            }
            else -> {
                logger.error { "unknown location type in $status" }
                throw RuntimeException("unknown location type in ${status.place}")
            }
        }
    }

    private fun GeoLocation.toGeoJsonPoint() = GeoPoint(longitude = this.longitude, latitude = this.latitude)
}
