package com.example.twittergrabber

import com.example.kafka.EnableMessageSender
import com.example.kafka.services.sender.MessageSender
import com.example.twittergrabber.services.TwitterReceiver
import com.example.twittergrabber.services.TwitterStatusesFilter
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

fun main(args: Array<String>) {
    runApplication<TwitterGrabberApplication>(*args)
}

@SpringBootApplication
@EnableMessageSender
class TwitterGrabberApplication {

    private companion object : KLogging()

    @Autowired
    private lateinit var twitterReceiver: TwitterReceiver
    @Autowired
    private lateinit var messageSender: MessageSender

    @Value("\${trackKeywords}")
    private lateinit var trackKeywords: String

    @PostConstruct
    fun init() {
        val twitterStreamRequest = TwitterStatusesFilter(trackKeywords = collectTrackKeywords())

        val messages = twitterReceiver
                .receiveStatuses(twitterStreamRequest)
                .flatMap(Converter::convertToMessage)

        messageSender.send(messages).subscribe()
    }

    private fun collectTrackKeywords(): Array<String> {
        return trackKeywords.lines().toTypedArray()
    }

}
