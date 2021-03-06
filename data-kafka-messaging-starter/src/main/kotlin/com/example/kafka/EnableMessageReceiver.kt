package com.example.kafka

import com.example.kafka.configuration.common.KafkaCommonConfig
import com.example.kafka.configuration.receiver.KafkaConsumerConfig
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(KafkaCommonConfig::class, KafkaConsumerConfig::class)
annotation class EnableMessageReceiver