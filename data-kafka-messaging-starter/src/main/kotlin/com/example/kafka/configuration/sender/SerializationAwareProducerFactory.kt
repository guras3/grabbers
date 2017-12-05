package com.example.kafka.configuration.sender

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.serialization.Serializer
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.internals.ProducerFactory

class SerializationAwareProducerFactory(private val keySerializer: Serializer<*>,
                                        private val valueSerializer: Serializer<*>) : ProducerFactory() {

    /**
     * да, да, костыли дикие
     * но пидор не я, пидоры авторы io.projectreactor.kafka/reactor-kafka
     */
    override fun <K, V> createProducer(senderOptions: SenderOptions<K, V>): Producer<K, V> {
        return KafkaProducer(senderOptions.producerProperties(), keySerializer as Serializer<K>, valueSerializer as Serializer<V>)
    }
}