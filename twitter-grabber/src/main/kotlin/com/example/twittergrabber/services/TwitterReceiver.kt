package com.example.twittergrabber.services

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.scheduler.Schedulers
import twitter4j.FilterQuery
import twitter4j.Status
import twitter4j.TwitterStreamFactory

@Component
class TwitterReceiver {

    private companion object : KLogging()

    @Autowired
    private lateinit var twitterStreamFactory: TwitterStreamFactory

    fun receiveStatuses(twitterStatusesFilter: TwitterStatusesFilter): Flux<Status> {
        val filterQuery = toFilterQuery(twitterStatusesFilter)

        val twitterStream = twitterStreamFactory.instance
        return Flux.push<Status>({ emitter ->
            twitterStream
                    .onException { ex -> logger.error(ex) { } }
                    .onStatus { emitter.next(it) }
                    .filter(filterQuery)
        }, FluxSink.OverflowStrategy.BUFFER)
                .publishOn(Schedulers.newSingle("twitter-grabber-worker"))
    }

    private fun toFilterQuery(twitterStatusesFilter: TwitterStatusesFilter): FilterQuery {
        return FilterQuery(
                twitterStatusesFilter.previousStatusesCount ?: 0,
                twitterStatusesFilter.followUserIds,
                twitterStatusesFilter.trackKeywords,
                twitterStatusesFilter.locations,
                twitterStatusesFilter.languages
        )
    }

}

