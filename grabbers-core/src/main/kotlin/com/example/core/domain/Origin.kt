package com.example.core.domain

import java.util.*

data class Origin(
        val service: ServiceEnum,
        val externalMessageId: String?,
        val creationDate: Date,
        val author: Author
)