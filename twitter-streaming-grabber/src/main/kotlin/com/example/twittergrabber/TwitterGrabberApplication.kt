package com.example.twittergrabber

import com.example.twittergrabber.domain.*
import com.example.twittergrabber.repository.Repository
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import twitter4j.FilterQuery
import twitter4j.TwitterStreamFactory
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableMongoRepositories
class TwitterGrabberApplication {

    private companion object : KLogging()

    @Autowired
    lateinit var repository: Repository
    @Autowired
    lateinit var twitterStreamFactory: TwitterStreamFactory

//    @PostConstruct
//    fun consume() {
//
//        val twitterStream = twitterStreamFactory.instance
//
//        val filterQuery = FilterQuery(
//                0,
//                null,
//                null,
//                arrayOf(doubleArrayOf(22.284086, 35.970508),
//                        doubleArrayOf(158.162992, 73.843899)),
//                arrayOf("ru"))
//
//        twitterStream.onException { ex -> logger.error(ex) { } }
//                .onStatus { status ->
//                    val post = Post(status.text, status.createdAt)
//                    repository.save(post)
//                }
//                .filter(filterQuery)
//    }

    @PostConstruct
    fun consume() {
        val post = Post(
                text = "текст хуекст текст хуекст текст хуекст текст хуекст текст хуекст",
                origin = Origin(
                        service = "TWITTER",
                        type = "POST",
                        serviceSpecificIdentifier = "856372",
                        creationDate = Date(),
                        author = Author(
                                name = "Вася",
                                serviceSpecificIdentifier = "4327553",
                                gender = "MALE"
                        )
                ),
                location = Location(
                        country = "Россия",
                        city = "Казань"
                ),
                popularity = Popularity(
                        likes = 23,
                        shares = 5
                )
        )

        repository.save(post)

    }
}

fun main(args: Array<String>) {
    runApplication<TwitterGrabberApplication>(*args)
}
