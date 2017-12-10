package com.example.kafka.services.sender

import com.example.domain.Message
import java.lang.Exception

data class SenderError(
        val message: Message,
        val exception: Exception
)