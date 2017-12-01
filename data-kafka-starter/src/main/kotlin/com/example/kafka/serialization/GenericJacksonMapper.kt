package com.example.kafka.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import java.io.IOException
import java.util.*

class GenericJacksonMapper<T>(private val targetClass: Class<T>,
                              private val objectMapper: ObjectMapper) : Deserializer<T>, Serializer<T> {

    override fun deserialize(topic: String, data: ByteArray?): T {
        if (data == null) throw SerializationException("Can't deserialize null value for topic [$topic]")
        return try {
            objectMapper.readValue(data, targetClass)
        } catch (ex: IOException) {
            throw SerializationException("Can't deserialize data [${Arrays.toString(data)}] from topic [$topic]", ex)
        }
    }

    override fun serialize(topic: String, data: T?): ByteArray {
        if (data == null) throw SerializationException("Can't serialize null value for topic [$topic]")
        return try {
            objectMapper.writeValueAsBytes(data)
        } catch (ex: IOException) {
            throw SerializationException("Can't serialize data [$data] for topic [$topic]", ex)
        }
    }

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) {
        // No-op
    }

    override fun close() {
        // No-op
    }
}