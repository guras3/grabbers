package com.example.kafka.configuration.consumers

//@ConfigurationProperties("data.kafka.consumers")
class KafkaConsumerProperties {

    var bootstrapServers: List<String>? = null
    var groupId: String? = null
    var concurrency: Int? = null

}