package com.example.kafka.config

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.util.*


@Configuration
@EnableKafka
class KafkaConsumerConfig {

    @Bean
    @ConditionalOnMissingBean
    fun jacksonObjectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule().also {
            it.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    @Bean
    @ConditionalOnMissingBean()
    fun messageJacksonMapper(): GenericJacksonMapper<Message> {
        return GenericJacksonMapper(Message::class.java, jacksonObjectMapper())
    }

    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Message>> {
        val props = HashMap<String, Any>()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "data-saver")
        val consumerFactory = DefaultKafkaConsumerFactory<String, Message>(props, StringDeserializer(), messageJacksonMapper())

        return ConcurrentKafkaListenerContainerFactory<String, Message>().also {
            it.consumerFactory = consumerFactory
            it.setConcurrency(4)
            it.containerProperties.pollTimeout = 3000
        }
    }

}