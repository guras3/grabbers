package com.example.kafka

import com.example.kafka.config.KafkaProducerConfig
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(KafkaProducerConfig::class)
annotation class EnableDataKafkaProducers