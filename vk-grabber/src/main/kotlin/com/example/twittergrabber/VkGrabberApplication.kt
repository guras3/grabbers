package com.example.twittergrabber

import com.example.twittergrabber.services.PersistentMessageQueue
import com.vk.api.sdk.streaming.clients.StreamingEventHandler
import com.vk.api.sdk.streaming.clients.VkStreamingApiClient
import com.vk.api.sdk.streaming.clients.actors.StreamingActor
import com.vk.api.sdk.streaming.objects.StreamingCallbackMessage
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import javax.annotation.PostConstruct


@SpringBootApplication
@EnableMongoRepositories
class VkGrabberApplication {

    private companion object : KLogging()

    @Autowired
    lateinit var streamingClient: VkStreamingApiClient

    @Autowired
    lateinit var streamingActor: StreamingActor

    @Autowired
    lateinit var persistentMessageQueue: PersistentMessageQueue

    @PostConstruct
    fun openStream() {
        streamingClient.stream().get(streamingActor, object : StreamingEventHandler() {
            override fun handle(message: StreamingCallbackMessage) {
                println(message)
            }
        }).execute()

    }

//    private fun convertToMessage(message: StreamingCallbackMessage) = Message(
//            text = message.event.text,
//            messageType = MessageTypeEnum.POST,
//            origin = Origin(
//                    service = ServiceEnum.TWITTER,
//                    externalMessageId = message.event.eventId,
//                    creationDate = message.event.creationTime,
//                    author = Author(
//                            externalAuthorId = message.,
//                            name = message.user.name,
//                            gender = GenderEnum.UNDEFINED
//                    )
//            ),
//            location = buildLocation(message)
//    )
//
//    private fun buildCoordinates(geoLocation: GeoLocation?): GeoJsonPoint? {
//        return geoLocation?.toGeoJsonPoint()
//    }
//
//    private fun buildLocation(status: Status): Location {
//        return when (status.place.boundingBoxType) {
//            "Polygon" -> {
//                val points = status.place.boundingBoxCoordinates[0].map { it.toGeoJsonPoint() }
//                //first and last position must be the same
//                val boundingBox = GeoJsonPolygon(points + points.first())
//                Location(country = status.place.country,
//                        locationType = status.place.placeType,
//                        locationName = status.place.fullName,
//                        boundingBox = boundingBox,
//                        exactCoordinates = buildCoordinates(status.geoLocation)
//                )
//            }
//            else -> {
//                logger.error { "unknown location type in $status" }
//                throw RuntimeException("unknown location type in ${status.place}")
//            }
//        }
//    }
//
//    private fun GeoLocation.toGeoJsonPoint(): GeoJsonPoint {
//        // GeoJSON specifies a longitude then a latitude,
//        return GeoJsonPoint(this.longitude, this.latitude)
//    }
}

fun main(args: Array<String>) {
    runApplication<VkGrabberApplication>(*args)
}
