package com.example.kafka.configuration.consumers

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.util.*

@Configuration
@EnableConfigurationProperties(KafkaConsumerProperties::class)
class KafkaConsumerConfig {

    @Bean
    fun kafkaListenerContainerFactory(messageJacksonMapper: GenericJacksonMapper<Message>,
                                      consumerProperties: KafkaConsumerProperties): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Message>> {
        val props = HashMap<String, Any>()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.bootstrapServers!!)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.groupId!!)
        val consumerFactory = DefaultKafkaConsumerFactory<String, Message>(props, StringDeserializer(), messageJacksonMapper)

        return ConcurrentKafkaListenerContainerFactory<String, Message>().also {
            it.consumerFactory = consumerFactory
            it.setConcurrency(consumerProperties.concurrency!!)
            it.containerProperties.pollTimeout = 3000
        }
    }

}