package com.example.datasaver.services

import com.example.datasaver.model.MongoMessage
import com.example.datasaver.repositories.MessageRepository
import com.example.domain.Message
import mu.KLogging
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

@Component
@Profile("prod")
@EnableScheduling
internal class BufferedPersistentMessageQueue(private val messageRepository: MessageRepository)  {

    private companion object : KLogging()

    private val queue = LinkedBlockingQueue<Message>(50)

    /* todo : read from kafka
    fun offer(message: Message): Boolean {
        val offered = queue.offer(message)
        if (!offered) {
            logger.warn { "message rejected due to queue overflow" }
        }
        return offered
    }*/

    @Scheduled(fixedDelay = 5_000)
    private fun persist() {
        if (queue.isEmpty()) return

        val batch = LinkedList<Message>()
        queue.drainTo(batch)
        logger.debug { "save batch size=${batch.size}" }
        val map: List<MongoMessage> = batch.map { MongoMessage.fromExternal(it) }
        messageRepository.saveAll(map)
    }

}