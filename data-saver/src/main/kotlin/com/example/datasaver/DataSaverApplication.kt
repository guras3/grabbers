package com.example.datasaver

import com.example.datasaver.model.MongoMessage
import com.example.kafka.EnableMessageReceiver
import com.example.kafka.services.receiver.MessageReceiver
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.SignalType
import java.util.logging.Level
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
    private lateinit var reactiveMongoTemplate: ReactiveMongoTemplate

    @PostConstruct
    fun saveMessages() {
        val messages = kafkaMessageReceiver.receive()
                .map(MongoMessage.Companion::fromExternal)
                .buffer(16)
                .flatMap { batch ->
                    reactiveMongoTemplate.insertAll(batch)
                            .log("error during insertAll", Level.SEVERE, SignalType.ON_ERROR)
                            .onErrorResume { Flux.empty() }
                }
                .subscribe()
    }

}