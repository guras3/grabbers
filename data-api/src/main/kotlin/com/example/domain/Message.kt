package com.example.domain

import java.util.*

data class Message(
        val text: String,
        val messageType: MessageTypeEnum,
        val origin: Origin,
        val location: Location?
) {
    val id: String = UUID.randomUUID().toString()
    val grabbedAt: Date = Date()
    val version = 1
}