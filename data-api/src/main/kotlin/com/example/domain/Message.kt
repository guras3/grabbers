package com.example.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

data class Message(
        val text: String,
        val messageType: MessageTypeEnum,
        val origin: Origin,
        val location: Location?
) {
    val guid: String? = null
    val grabbedAt: Date = Date()

}

@Document(collection = "messages")
data class MongoMessage(
        val message: Message
) {
    @Id
    val id = message.guid
    private val version: String = "1"
}