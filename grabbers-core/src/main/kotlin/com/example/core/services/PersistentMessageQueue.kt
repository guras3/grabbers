package com.example.core.services

import com.example.core.domain.Message

interface PersistentMessageQueue {

    fun offer(message: Message): Boolean

}
