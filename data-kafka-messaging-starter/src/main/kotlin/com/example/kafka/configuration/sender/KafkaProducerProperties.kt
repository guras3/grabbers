package com.example.kafka.configuration.sender

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("data.kafka.producers")
class KafkaProducerProperties {

    var bootstrapServers: List<String>? = null

}