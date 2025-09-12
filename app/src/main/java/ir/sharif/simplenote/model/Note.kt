package ir.sharif.simplenote.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(), // Auto-generate ID
    val title: String,
    val content: String,
    val lastModified: Long = System.currentTimeMillis() // Auto-set timestamp
)