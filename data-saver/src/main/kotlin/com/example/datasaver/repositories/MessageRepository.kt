package com.example.datasaver.repositories

import com.example.datasaver.model.MongoMessage
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

internal interface MessageRepository : ReactiveMongoRepository<MongoMessage, String> {
//    @Query("""
//        { ${'$'}and: [
//            { ${'$'}or: [
//                { "location.exactCoordinates" : { "${'$'}geoWithin" : { "${'$'}geometry" : ?0 } } },
//                { "location.polygon" : { "${'$'}geoIntersects" : { "${'$'}geometry" : ?0 } } }
//              ]
//            },
//            { "grabbedAt": { "${'$'}gte" : ?1 }}
//          ]
//        }
//    """)
//    fun findByCoordinatesWithin(polygon: GeoJsonPolygon, startDate: Date): List<Message>
}
