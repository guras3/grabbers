package com.example.twittergrabber.services

import com.example.domain.Message

interface PersistentMessageQueue {

    fun offer(message: Message): Boolean

}
