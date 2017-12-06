package com.example.kafka.services.sender

import com.example.domain.Message
import mu.KLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

class KafkaMessageSender(private val kafkaSender: KafkaSender<String, Message>) : MessageSender {

    private companion object : KLogging()

    override fun send(messages: Publisher<Message>): Flux<SenderError> {
        val kafkaMessages = Flux.from(messages)
                .map { SenderRecord.create(ProducerRecord("messages", it.id, it), it) }

        return kafkaSender.send(kafkaMessages)
                .flatMap(this::extractSenderError)
                .doOnNext { (message, ex) -> logger.error(ex) { "Can't send message for topic [messages] : \n$message\n" } }
    }

    private fun extractSenderError(senderResult: SenderResult<Message>): Mono<SenderError> {
        return if (senderResult.exception() != null) {
            Mono.just(SenderError(senderResult.correlationMetadata(), senderResult.exception()))
        } else Mono.empty()
    }

}

