package com.example.kafka.configuration.receiver

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.Deserializer
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.receiver.internals.ConsumerFactory

class SerializationAwareConsumerFactory(private val keyDeserializer: Deserializer<*>,
                                        private val valueDeserializer: Deserializer<*>) : ConsumerFactory() {

    /**
     * да, да, костыли дикие
     * но пидор не я, пидоры авторы io.projectreactor.kafka/reactor-kafka
     */
    override fun <K, V> createConsumer(receiverOptions: ReceiverOptions<K, V>): Consumer<K, V> {
        return KafkaConsumer(receiverOptions.consumerProperties(), keyDeserializer as Deserializer<K>, valueDeserializer as Deserializer<V>)
    }
}