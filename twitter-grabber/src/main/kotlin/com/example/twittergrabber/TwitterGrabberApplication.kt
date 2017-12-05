package com.example.twittergrabber

import com.example.kafka.EnableMessageSender
import com.example.kafka.services.sender.MessageSender
import com.example.twittergrabber.services.TwitterService
import com.example.twittergrabber.services.TwitterStreamRequest
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
    private lateinit var twitterService: TwitterService
    @Autowired
    private lateinit var messageSender: MessageSender

    @Value("\${trackKeywords}")
    private lateinit var trackKeywords: String

    @PostConstruct
    fun init() {
        Thread { openTwitterStream() }.start()
    }

    private fun openTwitterStream() {
        val twitterStreamRequest = TwitterStreamRequest(trackKeywords = collectTrackKeywords())

        val messages = twitterService
                .streamStatuses(twitterStreamRequest)
                .map(::convertToMessage)

        messageSender.send(messages)
    }

    fun collectTrackKeywords(): Array<String> {
        return trackKeywords.lines().toTypedArray()
    }

}
