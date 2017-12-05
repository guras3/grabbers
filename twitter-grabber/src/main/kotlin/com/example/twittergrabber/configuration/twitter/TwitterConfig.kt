package com.example.twittergrabber.configuration.twitter

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import twitter4j.TwitterStreamFactory
import twitter4j.conf.ConfigurationBuilder

@Configuration
@EnableConfigurationProperties(TwitterProperties::class)
class TwitterConfig {

    @Bean
    fun twitterStreamFactory(twitterProperties: TwitterProperties): TwitterStreamFactory {
        val configuration = ConfigurationBuilder()
                .setOAuthConsumerKey(twitterProperties.oauth.consumerKey)
                .setOAuthConsumerSecret(twitterProperties.oauth.consumerSecret)
                .setOAuthAccessToken(twitterProperties.oauth.accessToken)
                .setOAuthAccessTokenSecret(twitterProperties.oauth.accessTokenSecret)
                .build()
        return TwitterStreamFactory(configuration)
    }

}
