package com.example.demo.file

import java.time.Instant
import java.util.UUID

data class FileResponse(
    val id: UUID,
    val originalFileName: String,
    val contentType: String,
    val size: Long,
    val createdAt: Instant
)