package com.example.datasaver.model

import com.example.domain.ServiceEnum
import java.util.*

internal data class Origin(
        val service: ServiceEnum,
        val externalMessageId: String?,
        val creationDate: Date,
        val author: Author
) {
    companion object {
        fun fromExternal(external: com.example.domain.Origin): Origin {
            return Origin(
                    service = external.service,
                    externalMessageId = external.externalMessageId,
                    creationDate = external.creationDate,
                    author = Author.fromExternal(external.author)
            )
        }
    }
}