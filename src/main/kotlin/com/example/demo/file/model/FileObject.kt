package com.example.demo.file.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "files")
class FileObject(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val ownerId: UUID,

    @Column(nullable = false)
    var originalFileName: String,

    @Column(nullable = false, unique = true)
    val storageKey: String,

    @Column(nullable = false)
    val contentType: String,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    var deletedAt: Instant? = null
)