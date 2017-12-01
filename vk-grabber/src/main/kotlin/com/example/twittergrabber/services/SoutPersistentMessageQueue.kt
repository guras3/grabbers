package com.example.twittergrabber.services

import com.example.domain.Message
import com.example.twittergrabber.services.PersistentMessageQueue
import mu.KLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class SoutPersistentMessageQueue : PersistentMessageQueue {

    private companion object : KLogging()

    override fun offer(message: Message): Boolean {
        logger.info { message }
        return true
    }


}