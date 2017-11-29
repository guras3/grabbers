package com.example.datasaver

import com.example.domain.Message
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.util.*

@Configuration
@EnableKafka
class KafkaConfig {

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Int, Message>> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, Message>()
        factory.consumerFactory = consumerFactory()
        factory.setConcurrency(3)
        factory.containerProperties.pollTimeout = 3000
        return factory
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<Int, Message> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer::class.java)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonMessageSerializer::class.java)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "data-saver");
        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<Int, Message> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props = HashMap<String, Any>()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer::class.java)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonMessageSerializer::class.java)
        return props
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<Int, Message> {
        return KafkaTemplate(producerFactory())
    }

}

