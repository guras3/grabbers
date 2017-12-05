package com.example.twittergrabber

import com.example.domain.Message
import com.example.kafka.EnableDataKafkaReactiveProducers
import com.example.twittergrabber.services.TwitterService
import com.example.twittergrabber.services.TwitterStreamRequest
import mu.KLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import javax.annotation.PostConstruct

fun main(args: Array<String>) {
    runApplication<TwitterGrabberApplication>(*args)
}

@SpringBootApplication
@EnableDataKafkaReactiveProducers
class TwitterGrabberApplication {

    private companion object : KLogging()

    @Autowired
    private lateinit var twitterService: TwitterService
    @Autowired
    private lateinit var kafkaSender: KafkaSender<String, Message>

    @Value("\${trackKeywords}")
    private lateinit var trackKeywords: String

    @PostConstruct
    fun init() {
        Thread { openTwitterStream() }.start()
    }

    private fun openTwitterStream() {
        val twitterStreamRequest = TwitterStreamRequest(trackKeywords = collectTrackKeywords())

        val messagePublisher = twitterService
                .createPublisher(twitterStreamRequest)
                .map(::convertToMessage)

        val kafkaMessagePublisher = messagePublisher
                .map { ProducerRecord("messages", it.id, it) }
                .map { SenderRecord.create(it, null) }

        kafkaSender.send(kafkaMessagePublisher).subscribe()
    }


    fun collectTrackKeywords(): Array<String> {
        return trackKeywords.lines().toTypedArray()
    }

}
