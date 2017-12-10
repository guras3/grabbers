package com.example.kafka.services.receiver

import com.example.domain.Message
import reactor.core.publisher.Flux

interface MessageReceiver{

    fun receive(): Flux<Message>

}