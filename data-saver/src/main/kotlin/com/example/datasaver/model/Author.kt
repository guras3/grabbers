package com.example.datasaver.model

import com.example.domain.GenderEnum

internal data class Author(
        val externalAuthorId: String?,
        val name: String,
        val gender: GenderEnum
) {
    companion object {
        fun fromExternal(external: com.example.domain.Author): Author {
            return Author(
                    externalAuthorId = external.externalAuthorId,
                    name = external.name,
                    gender = external.gender
            )
        }
    }
}