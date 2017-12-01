package com.example.twittergrabber.configuration.vk

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vk")
class VkProperties {

    val oauth: OAuth = OAuth()

    class OAuth {
        lateinit var appId: String
        lateinit var accessToken: String
    }

}
