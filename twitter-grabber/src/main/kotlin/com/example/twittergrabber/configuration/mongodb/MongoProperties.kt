package com.example.twittergrabber.configuration.mongodb

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("mongo")
class MongoProperties {

    lateinit var host: String
    lateinit var port: String

}