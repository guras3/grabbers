package com.example.kafka.configuration.consumers

import org.springframework.context.annotation.Configuration

@Configuration
//@EnableConfigurationProperties(KafkaConsumerProperties::class)
class KafkaConsumerConfig {

//    @Bean
//    fun kafkaListenerContainerFactory(messageJacksonMapper: GenericJacksonMapper<Message>,
//                                      consumerProperties: KafkaConsumerProperties): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Message>> {
//        val props = HashMap<String, Any>()
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.bootstrapServers!!)
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.groupId!!)
//        val consumerFactory = DefaultKafkaConsumerFactory<String, Message>(props, StringDeserializer(), messageJacksonMapper)
//
//        return ConcurrentKafkaListenerContainerFactory<String, Message>().also {
//            it.consumerFactory = consumerFactory
//            it.setConcurrency(consumerProperties.concurrency!!)
//            it.containerProperties.pollTimeout = 3000
//        }
//    }

}