package com.example.demo.outbox.event

import java.time.Instant
import java.util.UUID

data class FileEvent(
    val fileId: UUID,
    val ownerId: UUID,
    val fileName: String,
    val eventType: String,
    val createdAt: Instant = Instant.now()
)