package com.example.twittergrabber.configuration.mongodb

import com.mongodb.MongoClientOptions
import org.springframework.context.annotation.Configuration
import com.mongodb.ServerAddress
import com.mongodb.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
@EnableConfigurationProperties(MongoProperties::class)
class MongoConfig() : AbstractMongoConfiguration() {

    @Autowired
    lateinit var mongoProperties: MongoProperties

    public override fun getDatabaseName(): String {
        return "posts"
    }

    override fun mongoClient(): MongoClient {
        val options = MongoClientOptions.builder()
                .connectionsPerHost(4)
                .minConnectionsPerHost(2)
                .build()

        return MongoClient(ServerAddress(mongoProperties.host, mongoProperties.port.toInt()), options)
    }
}
