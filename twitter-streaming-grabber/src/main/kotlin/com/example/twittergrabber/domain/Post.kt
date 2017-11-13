package com.example.twittergrabber.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "posts")
/* todo как назвать? */
data class Post(
        val text: String,
        val origin: Origin,
        val location: Location,
        val popularity: Popularity
) {
    @Id
    val id: String? = null
    val grabbedAt: Date = Date()
    private val version: Long = 1
}

data class Origin(
        /* todo enum TWITTER ; FACEBOOK ; etc... */
        val service: String,
        /* todo enum POST ; COMMENT ; etc... */
        val type: String,
        val serviceSpecificIdentifier: String?,
        val creationDate: Date,
        val author: Author
)

data class Author(
        val serviceSpecificIdentifier: String?,
        val name: String,
        /* todo enum MALE ; FEMALE */
        val gender: String
)

data class Location(
        val country: String,
        val city: String
)

data class Popularity(
        val likes: Long,
        val shares: Long
)
