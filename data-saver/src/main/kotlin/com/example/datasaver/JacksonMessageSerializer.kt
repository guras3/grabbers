package com.example.datasaver

import com.example.domain.Message
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

class JacksonMessageSerializer : Deserializer<Message>, Serializer<Message> {

    override fun serialize(topic: String, data: Message): ByteArray {
        return mapper.writeValueAsBytes(data)
    }

    val mapper = ObjectMapper().registerKotlinModule().also {
        it.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun deserialize(topic: String, data: ByteArray?): Message? {
        if (data == null) return null
        return mapper.readValue(data, Message::class.java)
    }

    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {

    }

    override fun close() {

    }
}