package com.example.datasaver.configuration.mongodb

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("mongo")
class MongoProperties {

    lateinit var host: String
    lateinit var port: String

}