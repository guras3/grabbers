package com.example.twittergrabber

import com.example.domain.*
import com.example.kafka.EnableDataKafkaProducers
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${trackKeywords}")
    lateinit var trackKeywords: String

    @PostConstruct
    fun init() {
        Thread { openTwitterStream() }.start()
    }

    fun collectTrackKeywords(): Array<String> {
        return trackKeywords.lines().toTypedArray()
    }

    private fun openTwitterStream() {
        val twitterStream = twitterStreamFactory.instance

        val trackKeywords = collectTrackKeywords()

        val filterQuery = FilterQuery(
                0,
                null,
                trackKeywords,
                null,
                null)

        twitterStream
                .onException { ex -> logger.error(ex) { } }
                .onStatus(this::convertAndSend)
                .filter(filterQuery)
    }

    private fun convertAndSend(status: Status) {
        val message = convertToMessage(status)
        logger.debug { "sending message: [$message]" }
        kafka.send("messages", message.id, message)
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
}
