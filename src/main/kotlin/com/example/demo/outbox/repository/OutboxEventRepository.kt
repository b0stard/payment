package com.example.demo.outbox.repository


import com.example.demo.outbox.model.OutboxEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OutboxEventRepository : JpaRepository<OutboxEvent, UUID> {

    fun findTop50ByProcessedFalseOrderByCreatedAtAsc(): List<OutboxEvent>
}