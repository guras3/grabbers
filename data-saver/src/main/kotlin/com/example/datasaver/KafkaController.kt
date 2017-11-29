package com.example.datasaver

import com.example.domain.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.annotation.Order
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import java.util.*


@Component
@Order(1)
class KafkaController {

    @KafkaListener(topics = arrayOf("messages"))
    fun listen(message: Message) {
        println(message)
    }

}

/* FOR TESTING PURPOSES */
@Component
@Order(2)
class KafkaProducer {

    @Autowired
    lateinit var template: KafkaTemplate<Int, Message>

    @Scheduled(fixedDelay = 1000)
    fun send() {
        val message = Message(
                text = "sample",
                messageType = MessageTypeEnum.POST,
                origin = Origin(
                        service = ServiceEnum.TWITTER,
                        externalMessageId = "123",
                        creationDate = Date(),
                        author = Author(
                                externalAuthorId = "124",
                                name = "jopa",
                                gender = GenderEnum.MALE
                        )
                ),
                location = null
        )

        template.send("messages", message)
    }

}