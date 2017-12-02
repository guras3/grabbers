package com.example.kafka

import com.example.kafka.configuration.common.KafkaCommonConfig
import com.example.kafka.configuration.consumers.KafkaConsumerConfig
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.KafkaBootstrapConfiguration

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(KafkaCommonConfig::class, KafkaConsumerConfig::class, KafkaBootstrapConfiguration::class)
annotation class EnableDataKafkaConsumers