package com.example.datasaver

import com.example.datasaver.model.MongoMessage
import com.example.datasaver.repositories.MessageRepository
import com.example.kafka.EnableMessageReceiver
import com.example.kafka.services.receiver.MessageReceiver
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

fun main(args: Array<String>) {
    runApplication<DataSaverApplication>(*args)
}

@SpringBootApplication
@EnableMessageReceiver
class DataSaverApplication {

    private companion object : KLogging()

    @Autowired
    private lateinit var kafkaMessageReceiver: MessageReceiver
    @Autowired
    private lateinit var messageRepository: MessageRepository

    @PostConstruct
    fun init() {
        Thread { saveMessages() }.start()
    }

    fun saveMessages() {
        val messages = kafkaMessageReceiver.receive()
                .map(MongoMessage.Companion::fromExternal)

        messageRepository.saveAll(messages).retry().subscribe()
    }

}