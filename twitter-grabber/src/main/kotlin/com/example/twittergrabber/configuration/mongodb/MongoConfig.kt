package com.example.twittergrabber.configuration.mongodb

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
        return "data_set"
    }

    override fun mongoClient(): MongoClient {
        val options = MongoClientOptions.builder()
                .connectionsPerHost(4)
                .minConnectionsPerHost(2)
                .build()

        return MongoClient(ServerAddress(mongoProperties.host, mongoProperties.port.toInt()), options)
    }

    override fun mappingMongoConverter(): MappingMongoConverter {
        return super.mappingMongoConverter().also {
            it.typeMapper = DefaultMongoTypeMapper(null)

        }
    }

    /*override fun customConversions(): CustomConversions {
        val converterList = ArrayList<Converter<*, *>>()
        converterList.add(org.springframework.data.mongodb.test.PersonReadConverter())
        converterList.add(org.springframework.data.mongodb.test.PersonWriteConverter())
        return CustomConversions(converterList)
    }

    inner class PersonReadConverter : Converter<Document, Person> {

        override fun convert(source: Document): Person? {
            val p = Person(source.get("_id") as ObjectId, source.get("name") as String)
            p.setAge(source.get("age") as Int)
            return p
        }
    }*/
}
