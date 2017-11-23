package com.example.core.repositories

import com.example.core.domain.Message
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*


interface MessageRepository : MongoRepository<Message, String> {
    @Query("""
        { ${'$'}and: [
            { ${'$'}or: [
                { "location.exactCoordinates" : { "${'$'}geoWithin" : { "${'$'}geometry" : ?0 } } },
                { "location.boundingBox" : { "${'$'}geoIntersects" : { "${'$'}geometry" : ?0 } } }
              ]
            },
            { "grabbedAt": { "${'$'}gte" : ?1 }}
          ]
        }
    """)
    fun findByCoordinatesWithin(polygon: GeoJsonPolygon, startDate: Date): List<Message>
}
