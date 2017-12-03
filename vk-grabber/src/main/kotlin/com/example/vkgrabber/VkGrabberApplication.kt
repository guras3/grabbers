package com.example.vkgrabber

import com.fasterxml.jackson.databind.ObjectMapper
import com.vk.api.sdk.streaming.clients.StreamingEventHandler
import com.vk.api.sdk.streaming.clients.VkStreamingApiClient
import com.vk.api.sdk.streaming.clients.actors.StreamingActor
import com.vk.api.sdk.streaming.objects.StreamingCallbackMessage
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

fun main(args: Array<String>) {
    runApplication<VkGrabberApplication>(*args)
}

@SpringBootApplication
class VkGrabberApplication {

    private companion object : KLogging()

    @Autowired
    lateinit var streamingClient: VkStreamingApiClient

    @Autowired
    lateinit var streamingActor: StreamingActor

    @PostConstruct
    fun openStream() {
        val om = ObjectMapper()
        streamingClient.stream().get(streamingActor, object : StreamingEventHandler() {
            override fun handle(message: StreamingCallbackMessage) {
                println(om.writeValueAsString(message))
            }
        }).execute()

    }

}