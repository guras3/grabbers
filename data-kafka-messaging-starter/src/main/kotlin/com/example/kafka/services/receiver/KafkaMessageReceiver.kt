package com.example.kafka.services.receiver

import com.example.domain.Message
import reactor.core.publisher.Flux

class KafkaMessageReceiver(private val kafkaReceiverFactory: KafkaReceiverFactory, private val concurrency: Int) : MessageReceiver {

    override fun receive(): Flux<Message> {
        val superReceiver = generateSequence { kafkaReceiverFactory.createReceiver().receive() }
                .take(concurrency)
                .reduce { acc, flux -> acc.mergeWith(flux) }

        return superReceiver.map { it.value() }
    }

}