package com.example.datasaver

import com.example.datasaver.model.MongoMessage
import com.example.datasaver.repositories.MessageRepository
import com.example.domain.Message
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaController {

    private companion object : KLogging()

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @KafkaListener(topics = arrayOf("messages"))
    fun listen(message: Message) {
        val mongoMessage = MongoMessage.fromExternal(message)
        logger.info { "received message with id [${mongoMessage.id}]" }
        messageRepository.save(mongoMessage)
    }

}
