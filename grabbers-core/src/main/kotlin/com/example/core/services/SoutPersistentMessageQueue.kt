package com.example.core.services

import com.example.core.domain.Message
import com.example.core.repositories.MessageRepository
import mu.KLogging
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

@Component
class SoutPersistentMessageQueue: PersistentMessageQueue {

    private companion object : KLogging()

    override fun offer(message: Message): Boolean {
        logger.info { message }
        return true
    }


}