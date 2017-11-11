package com.example.twittergrabber.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.annotation.Generated

@Document(collection = "posts")
data class Post(
        val text: String,
        val createdAt: Date,
        @Id @Generated("guid")
        val id: String? = null
)