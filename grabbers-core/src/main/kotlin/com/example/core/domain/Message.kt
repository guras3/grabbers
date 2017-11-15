package com.example.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "messages")
data class Message(
        val text: String,
        val messageType: MessageTypeEnum,
        val origin: Origin,
        val location: Location
) {
    @Id
    val guid: String? = null
    val grabbedAt: Date = Date()
    private val version: Long = 1
}
