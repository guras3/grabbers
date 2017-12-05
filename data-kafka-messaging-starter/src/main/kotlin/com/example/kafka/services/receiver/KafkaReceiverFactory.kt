package com.example.kafka.services.receiver

import com.example.domain.Message
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.receiver.internals.ConsumerFactory
import reactor.kafka.receiver.internals.DefaultKafkaReceiver

class KafkaReceiverFactory(private val receiverOptions: ReceiverOptions<String, Message>,
                           private val consumerFactory: ConsumerFactory) {

    fun createReceiver(): KafkaReceiver<String, Message> {
        val defaultKafkaReceiver = DefaultKafkaReceiver(consumerFactory, receiverOptions)
        return defaultKafkaReceiver
    }

}