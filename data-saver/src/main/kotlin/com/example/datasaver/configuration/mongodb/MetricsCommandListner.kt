package com.example.datasaver.configuration.mongodb

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricRegistry
import com.mongodb.event.CommandFailedEvent
import com.mongodb.event.CommandListener
import com.mongodb.event.CommandStartedEvent
import com.mongodb.event.CommandSucceededEvent
import mu.KLogging
import java.util.concurrent.TimeUnit

object MetricsCommandListner : CommandListener, KLogging() {
    val registry = MetricRegistry()
    val reporter = JmxReporter.forRegistry(registry).build().also {
        it.start()
    };

    val commandStarted = registry.meter("commandStarted")
    val commandFailed = registry.meter("commandFailed")
    val commandSucceeded = registry.meter("commandSucceeded")

    override fun commandStarted(event: CommandStartedEvent) {
        logger.debug { "commandStarted requestId=${event.requestId} ${event.command}" }
        commandStarted.mark()
    }

    override fun commandFailed(event: CommandFailedEvent) {
        logger.debug { "commandFailed requestId=${event.requestId} took=${event.getElapsedTime(TimeUnit.MILLISECONDS)}ms, error=${event.throwable}" }
        commandFailed.mark()
    }

    override fun commandSucceeded(event: CommandSucceededEvent) {
        logger.debug { "commandSucceeded requestId=${event.requestId} took=${event.getElapsedTime(TimeUnit.MILLISECONDS)}ms, response=${event.response}" }
        commandSucceeded.mark()
    }
}