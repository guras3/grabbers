package com.example.kafka.configuration.sender

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import com.example.kafka.services.sender.KafkaMessageSender
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.internals.DefaultKafkaSender
import java.util.*

@Configuration
@EnableConfigurationProperties(KafkaProducerProperties::class)
class KafkaProducerConfig {

    @Bean
    fun kafkaMessageSender(messageJacksonMapper: GenericJacksonMapper<Message>,
                           producerProperties: KafkaProducerProperties): KafkaMessageSender {
        return KafkaMessageSender(kafkaSender(messageJacksonMapper, producerProperties))
    }

    @Bean
    fun kafkaSender(messageJacksonMapper: GenericJacksonMapper<Message>,
                    producerProperties: KafkaProducerProperties): KafkaSender<String, Message> {

        val producerProps = HashMap<String, Any>()
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.bootstrapServers!!)

        val senderOptions = SenderOptions.create<String, Message>(producerProps).maxInFlight(1024)

        val factory = SerializationAwareProducerFactory(StringSerializer(), messageJacksonMapper)
        val sender = DefaultKafkaSender(factory, senderOptions)

        return sender
    }

}

