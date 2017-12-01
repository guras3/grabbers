package com.example.datasaver.model

import com.example.domain.Message
import com.example.domain.MessageTypeEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "messages")
internal data class MongoMessage(
        @Id
        val id: String,
        val text: String,
        val messageType: MessageTypeEnum,
        val origin: Origin,
        val location: Location?,
        val grabbedAt: Date,
        val version: Int
) {
    companion object {
        fun fromExternal(external: Message): MongoMessage {
            return MongoMessage(
                    id = external.id,
                    text = external.text,
                    messageType = external.messageType,
                    origin = Origin.fromExternal(external.origin),
                    location = external.location?.let { Location.fromExternal(it) },
                    grabbedAt = external.grabbedAt,
                    version = external.version
            )
        }
    }
}
