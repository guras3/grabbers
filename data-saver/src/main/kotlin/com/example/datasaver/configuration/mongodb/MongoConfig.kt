package com.example.datasaver.configuration.mongodb

import com.mongodb.ServerAddress
import com.mongodb.async.client.MongoClientSettings
import com.mongodb.connection.ClusterConnectionMode
import com.mongodb.connection.ClusterSettings
import com.mongodb.connection.ClusterType
import com.mongodb.connection.ConnectionPoolSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import java.util.concurrent.TimeUnit

@Configuration
@EnableConfigurationProperties(MongoProperties::class)
class MongoConfig : AbstractReactiveMongoConfiguration() {

    @Autowired
    lateinit var mongoProperties: MongoProperties

    @Bean
    override fun reactiveMongoClient(): MongoClient {
        val clusterSettings = ClusterSettings.builder()
                .requiredClusterType(ClusterType.STANDALONE)
                .mode(ClusterConnectionMode.SINGLE)
                .hosts(listOf(ServerAddress(mongoProperties.host, mongoProperties.port.toInt())))
                .build()

        val connectionPoolSettings = ConnectionPoolSettings.builder()
                .minSize(4)
                .maxSize(8)
                .maxConnectionLifeTime(0, TimeUnit.MILLISECONDS)
                .maxConnectionIdleTime(1, TimeUnit.MINUTES)
                .maxWaitQueueSize(500)
                .maxWaitTime(20, TimeUnit.SECONDS)
                .build()

        val settings = MongoClientSettings.builder()
                .clusterSettings(clusterSettings)
                .connectionPoolSettings(connectionPoolSettings)
                .build()

        return MongoClients.create(settings)
    }

    override fun getDatabaseName(): String {
        return mongoProperties.databaseName
    }

    /**
     * хак чтобы не писать поле _class
     */
    override fun mappingMongoConverter(): MappingMongoConverter {
        return super.mappingMongoConverter().also {
            it.typeMapper = DefaultMongoTypeMapper(null)
        }
    }

}
