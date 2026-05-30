package com.example.demo.outbox.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "outbox_events")
class OutboxEvent(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val aggregateId: UUID,

    @Column(nullable = false)
    val aggregateType: String,

    @Column(nullable = false)
    val eventType: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val payload: String,

    @Column(nullable = false)
    var processed: Boolean = false,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    var processedAt: Instant? = null
)