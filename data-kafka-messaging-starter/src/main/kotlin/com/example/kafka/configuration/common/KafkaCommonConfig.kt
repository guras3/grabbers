package com.example.kafka.configuration.common

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaCommonConfig {

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

}