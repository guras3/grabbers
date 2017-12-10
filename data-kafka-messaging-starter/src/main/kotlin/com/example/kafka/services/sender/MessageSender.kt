package com.example.kafka.services.sender

import com.example.domain.Message
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

interface MessageSender {

    fun send(messages: Publisher<Message>): Flux<SenderError>

}
