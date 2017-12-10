package com.example.kafka

import com.example.kafka.configuration.common.KafkaCommonConfig
import com.example.kafka.configuration.sender.KafkaProducerConfig
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(KafkaCommonConfig::class, KafkaProducerConfig::class)
annotation class EnableMessageSender