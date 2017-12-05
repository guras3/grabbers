package com.example.kafka.services.sender

import com.example.domain.Message
import org.reactivestreams.Publisher

interface MessageSender {

    fun send(messages: Publisher<Message>)

}
