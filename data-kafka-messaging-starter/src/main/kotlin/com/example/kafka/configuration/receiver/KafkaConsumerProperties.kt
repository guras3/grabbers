package com.example.kafka.configuration.receiver

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("data.kafka.consumers")
class KafkaConsumerProperties {

    var bootstrapServers: List<String>? = null
    var groupId: String? = null
    var concurrency: Int? = null

}