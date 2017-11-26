package com.example.twittergrabber.configuration.twitter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("twitter")
class TwitterProperties {

    val oauth: OAuth = OAuth()

    class OAuth {
        lateinit var consumerKey: String
        lateinit var consumerSecret: String
        lateinit var accessToken: String
        lateinit var accessTokenSecret: String
    }

}
