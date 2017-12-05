package com.example.kafka.services.sender

import com.example.domain.Message
import org.apache.kafka.clients.producer.ProducerRecord
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

class KafkaMessageSender(private val kafkaSender: KafkaSender<String, Message>) : MessageSender {

    override fun send(messages: Publisher<Message>) {
        val kafkaMessagePublisher = Flux.from(messages)
                .map { ProducerRecord("messages", it.id, it) }
                .map { SenderRecord.create(it, null) }


        kafkaSender.send(kafkaMessagePublisher).subscribe()
    }

}
