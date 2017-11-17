package com.example.twittergrabber

import com.example.core.domain.*
import com.example.core.services.PersistentMessageQueue
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import twitter4j.FilterQuery
import twitter4j.Status
import twitter4j.TwitterStreamFactory
import javax.annotation.PostConstruct

@SpringBootApplication(scanBasePackages = arrayOf(
        "com.example.core",
        "com.example.twittergrabber"
))
@EnableMongoRepositories(basePackages = arrayOf("com.example.core.repositories"))
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
            location = Location(
                    country = status.place.country,
                    locationType = status.place.placeType,
                    locationName = status.place.fullName
            )
    )

}

fun main(args: Array<String>) {
    runApplication<TwitterGrabberApplication>(*args)
}
