package com.example.core.repositories

import com.example.core.domain.Message
import org.springframework.data.mongodb.repository.MongoRepository

internal interface MessageRepository : MongoRepository<Message, String>