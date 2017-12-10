package com.example.twittergrabber.services

import mu.KLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.scheduler.Schedulers
import twitter4j.FilterQuery
import twitter4j.Status
import twitter4j.TwitterStreamFactory

@Component
class TwitterReceiver(private val twitterStreamFactory: TwitterStreamFactory) {

    private companion object : KLogging()

    fun receiveStatuses(twitterStatusesFilter: TwitterStatusesFilter): Flux<Status> {
        val filterQuery = toFilterQuery(twitterStatusesFilter).also {
            logger.info { "Starting consuming tweets with $it" }
        }

        val twitterStream = twitterStreamFactory.instance
        return Flux.push<Status>(
                { emitter ->
                    twitterStream
                            .onException { ex -> logger.error(ex, { "error during receiving status" }) }
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

