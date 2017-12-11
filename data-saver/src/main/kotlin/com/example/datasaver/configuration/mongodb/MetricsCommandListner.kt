package com.example.datasaver.configuration.mongodb

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricRegistry
import com.mongodb.event.CommandFailedEvent
import com.mongodb.event.CommandListener
import com.mongodb.event.CommandStartedEvent
import com.mongodb.event.CommandSucceededEvent
import mu.KLogging
import org.bson.BsonInt32
import java.util.concurrent.TimeUnit

object MetricsCommandListner : CommandListener, KLogging() {
    val registry = MetricRegistry()
    val reporter = JmxReporter.forRegistry(registry).build().also {
        it.start()
    };

    override fun commandStarted(event: CommandStartedEvent) {
        logger.debug { "commandStarted requestId=${event.requestId} ${event.command}" }
        val cnt = when (event.commandName) {
            "insert" -> event.command.getArray("documents").size.toLong()
            else -> 1L
        }

        registry.meter("started:${event.commandName}")
                .mark(cnt)
    }

    override fun commandFailed(event: CommandFailedEvent) {
        logger.debug { "commandFailed requestId=${event.requestId} took=${event.getElapsedTime(TimeUnit.MILLISECONDS)}ms, error=${event.throwable}" }
        registry.meter("failed:${event.commandName}").mark()
    }

    override fun commandSucceeded(event: CommandSucceededEvent) {
        logger.debug { "commandSucceeded requestId=${event.requestId} took=${event.getElapsedTime(TimeUnit.MILLISECONDS)}ms, response=${event.response}" }
        registry.meter("succeeded:${event.commandName}")
                .mark(event.response.getNumber("n", BsonInt32(1)).longValue())
    }
}