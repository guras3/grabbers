package com.example.vkgrabber

import com.fasterxml.jackson.databind.ObjectMapper
import com.vk.api.sdk.streaming.clients.StreamingEventHandler
import com.vk.api.sdk.streaming.clients.VkStreamingApiClient
import com.vk.api.sdk.streaming.clients.actors.StreamingActor
import com.vk.api.sdk.streaming.clients.websocket.WSMessageListener
import com.vk.api.sdk.streaming.clients.websocket.WSPingListener
import com.vk.api.sdk.streaming.objects.StreamingCallbackMessage
import com.vk.api.sdk.streaming.objects.StreamingRule
import com.vk.api.sdk.streaming.objects.responses.StreamingGetRulesResponse
import com.vk.api.sdk.streaming.objects.responses.StreamingResponse
import mu.KLogging
import org.asynchttpclient.ws.WebSocketUpgradeHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.InputStreamReader
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
    fun init() {
        createRulesIfNeeded()
        Thread { openVkStream() }.start()
    }

    fun openVkStream() {
        val om = ObjectMapper()

        val url = "wss://" + streamingActor.endpoint + "/stream?key=" + streamingActor.key

        val idiotHandler = object : StreamingEventHandler() {
            override fun handle(message: StreamingCallbackMessage) {
                println(om.writeValueAsString(message))
            }
        }

        val listener = object : WSMessageListener(streamingClient.gson, idiotHandler) {
            override fun onError(t: Throwable?) {
                logger.info { "something crashed... reopening stream..." }
                openVkStream()
            }
        }

        streamingClient.asyncHttpClient.prepareGet(url).execute(
                WebSocketUpgradeHandler.Builder()
                        .addWebSocketListener(listener)
                        .addWebSocketListener(WSPingListener())
                        .build()
        ).get()
    }

    fun createRulesIfNeeded() {
        val rules = streamingClient.rules().get(streamingActor).execute<StreamingGetRulesResponse>().rules
        if (rules.isEmpty()) {
            logger.info { "Rules not configured. Configuring rules..." }
            createRules()
            logger.info { "Rules created successfully." }
        } else if (rules.size < 300) {
            logger.info { "Rules count < 300. Deleting all rules..." }
            removeRules(rules)
            logger.info { "All rules deleted successfully. Recreating rules..." }
            createRules()
            logger.info { "Rules created successfully." }
        }
    }

    fun createRules() {
        val inputStream = this.javaClass.classLoader.getResourceAsStream("rules.txt")
        BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().forEach {
                val (tag, word) = it.split(";")
                streamingClient.rules().add(streamingActor, tag, word).execute<StreamingResponse>()
            }
        }
    }

    private fun removeRules(rules: List<StreamingRule>) {
        rules.forEach {
            streamingClient.rules().delete(streamingActor, it.tag)
        }
    }

}
