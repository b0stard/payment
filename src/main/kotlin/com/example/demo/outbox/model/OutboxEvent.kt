package com.example.demo.outbox.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "outbox_events")
class OutboxEvent(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var aggregateId: UUID? = null,

    @Column(nullable = false)
    var eventType: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var payload: String = "",

    @Column(nullable = false)
    var processed: Boolean = false,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now()
)