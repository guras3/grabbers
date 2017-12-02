package com.example.kafka.configuration.producers

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

@Configuration
@EnableConfigurationProperties(KafkaProducerProperties::class)
class KafkaProducerConfig {

    @Bean
    fun kafkaTemplate(messageJacksonMapper: GenericJacksonMapper<Message>,
                      producerProperties: KafkaProducerProperties): KafkaTemplate<String, Message> {
        val props = HashMap<String, Any>()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.bootstrapServers!!)
        val producerFactory = DefaultKafkaProducerFactory(props, StringSerializer(), messageJacksonMapper)
        return KafkaTemplate(producerFactory)
    }

}

