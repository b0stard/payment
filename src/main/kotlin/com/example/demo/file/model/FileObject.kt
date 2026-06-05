package com.example.demo.file.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "files")
class FileObject(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var ownerId: UUID,

    @Column(nullable = false)
    var originalFileName: String,

    @Column(nullable = false, unique = true)
    var storageKey: String,

    @Column(nullable = false)
    var contentType: String,

    @Column(nullable = false)
    var size: Long,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    var deletedAt: Instant? = null
)