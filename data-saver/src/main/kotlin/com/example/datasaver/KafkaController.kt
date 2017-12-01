package com.example.datasaver

import com.example.domain.Message
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaController {

    @KafkaListener(topics = arrayOf("messages"))
    fun listen(message: Message) {
        println(message)
    }

}
