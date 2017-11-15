package com.example.core.domain

data class Author(
        val externalAuthorId: String?,
        val name: String,
        val gender: GenderEnum
)