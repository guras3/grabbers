package com.example.kafka.configuration.producers

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("data.kafka.producers")
class KafkaProducerProperties {

    var bootstrapServers: List<String>? = null

}