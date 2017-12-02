package com.example.datasaver.configuration.mongodb

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.ServerAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter

@Configuration
@EnableConfigurationProperties(MongoProperties::class)
class MongoConfig : AbstractMongoConfiguration() {

    @Autowired
    lateinit var mongoProperties: MongoProperties

    public override fun getDatabaseName(): String {
        return mongoProperties.databaseName
    }

    /**
     * todo - разобраться, что нам нужно конфигурить и вынести в MongoProperties
     * todo - либо разобраться, как сделать через автоконфигурацию
     *
     * todo - разобраться с авторизацией
     */
    override fun mongoClient(): MongoClient {
        val options = MongoClientOptions.builder()
                .connectionsPerHost(4)
                .minConnectionsPerHost(2)
                .build()

        return MongoClient(ServerAddress(mongoProperties.host, mongoProperties.port.toInt()), options)
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
