package com.example.demo.file.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "files")
class FileObject(

    @Id
    var id: UUID? = null,

    @Column(nullable = false)
    var ownerId: UUID? = null,

    @Column(nullable = false)
    var originalFileName: String = "",

    @Column(nullable = false, unique = true)
    var storageKey: String = "",

    @Column(nullable = false)
    var contentType: String = "",

    @Column(nullable = false)
    var size: Long = 0,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    var deletedAt: Instant? = null
)