package com.example.kafka.config

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

@Configuration
@EnableKafka
class KafkaProducerConfig {

    @Bean
    @ConditionalOnMissingBean
    fun jacksonObjectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule().also {
            it.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun messageJacksonMapper(): GenericJacksonMapper<Message> {
        return GenericJacksonMapper(Message::class.java, jacksonObjectMapper())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Message> {
        val props = HashMap<String, Any>()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
        val producerFactory = DefaultKafkaProducerFactory(props, StringSerializer(), messageJacksonMapper())
        return KafkaTemplate(producerFactory)
    }

}

