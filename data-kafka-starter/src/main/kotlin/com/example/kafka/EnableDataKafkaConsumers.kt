package com.example.kafka

import com.example.kafka.config.KafkaConsumerConfig
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(KafkaConsumerConfig::class)
annotation class EnableDataKafkaConsumers