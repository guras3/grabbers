package com.example.kafka.configuration.receiver

import com.example.domain.Message
import com.example.kafka.serialization.GenericJacksonMapper
import com.example.kafka.services.receiver.KafkaMessageReceiver
import com.example.kafka.services.receiver.KafkaReceiverFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.ReceiverOptions
import java.util.*


@Configuration
@EnableConfigurationProperties(KafkaConsumerProperties::class)
class KafkaConsumerConfig {

    @Bean
    fun kafkaReceiverFactory(messageJacksonMapper: GenericJacksonMapper<Message>,
                             consumerProperties: KafkaConsumerProperties): KafkaReceiverFactory {

        val props = HashMap<String, Any>()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.bootstrapServers!!)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.groupId!!)

        val receiverOptions = ReceiverOptions.create<String, Message>(props)
                .subscription(Collections.singleton("messages"))

        val consumerFactory = SerializationAwareConsumerFactory(StringDeserializer(), messageJacksonMapper)

        return KafkaReceiverFactory(receiverOptions, consumerFactory)
    }

    @Bean
    fun kafkaMessageReceiver(messageJacksonMapper: GenericJacksonMapper<Message>,
                             consumerProperties: KafkaConsumerProperties): KafkaMessageReceiver {
        return KafkaMessageReceiver(kafkaReceiverFactory(messageJacksonMapper, consumerProperties), consumerProperties.concurrency!!)
    }


}
