package com.example.twittergrabber.services

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import twitter4j.FilterQuery
import twitter4j.Status
import twitter4j.TwitterStreamFactory

@Component
class TwitterService {

    private companion object : KLogging()

    @Autowired
    private lateinit var twitterStreamFactory: TwitterStreamFactory

    fun createPublisher(twitterStreamRequest: TwitterStreamRequest): Flux<Status> {
        val filterQuery = toFilterQuery(twitterStreamRequest)

        val twitterStream = twitterStreamFactory.instance
        return Flux.push<Status>({ emitter ->
            twitterStream
                    .onException { ex -> logger.error(ex) { } }
                    .onStatus { emitter.next(it) }
                    .filter(filterQuery)
        }, FluxSink.OverflowStrategy.ERROR)

    }

    private fun toFilterQuery(twitterStreamRequest: TwitterStreamRequest): FilterQuery {
        return FilterQuery(
                twitterStreamRequest.previousStatusesCount ?: 0,
                twitterStreamRequest.followUserIds,
                twitterStreamRequest.trackKeywords,
                twitterStreamRequest.locations,
                twitterStreamRequest.languages
        )
    }

}

