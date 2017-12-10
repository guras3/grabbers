package com.example.twittergrabber.services

data class TwitterStatusesFilter(
        val previousStatusesCount: Int? = null,
        val followUserIds: LongArray? = null,
        val trackKeywords: Array<String>? = null,
        val locations: Array<DoubleArray>? = null,
        val languages: Array<String>? = null
)