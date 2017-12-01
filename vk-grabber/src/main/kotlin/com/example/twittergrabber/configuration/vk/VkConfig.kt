package com.example.twittergrabber.configuration.vk

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.streaming.clients.VkStreamingApiClient
import com.vk.api.sdk.streaming.clients.actors.StreamingActor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(VkProperties::class)
class VkConfig {

    private companion object {
        val transportClient = HttpTransportClient()
    }

    @Bean
    fun vkClientFactory(): VkApiClient {
        return VkApiClient(transportClient)
    }

    @Bean
    fun VkStreamingApiClientFactory(): VkStreamingApiClient {
        return VkStreamingApiClient(transportClient)
    }

    @Bean
    fun vkStreamingActorFactory(vkProperties: VkProperties): StreamingActor {
        val vkClient = vkClientFactory()
        val actor = ServiceActor(vkProperties.oauth.appId.toInt(), vkProperties.oauth.accessToken)

        val getServerUrlResponse = vkClient.streaming().getServerUrl(actor).
                also { it.method }.execute()
        return StreamingActor(getServerUrlResponse.endpoint, getServerUrlResponse.key)
    }

}
