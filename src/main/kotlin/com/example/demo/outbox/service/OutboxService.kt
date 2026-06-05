package com.example.demo.outbox.service

import com.example.demo.outbox.model.OutboxEvent
import com.example.demo.outbox.event.FileEvent
import com.example.demo.outbox.repository.OutboxEventRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class OutboxService(
    private val outboxEventRepository: OutboxEventRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun createFileEvent(event: FileEvent) {
        val outboxEvent = OutboxEvent(
            aggregateId = event.fileId,
            eventType = event.eventType,
            payload = objectMapper.writeValueAsString(event)
        )

        outboxEventRepository.save(outboxEvent)
    }

    @Transactional
    fun getPendingEvents(): List<OutboxEvent> {
        return outboxEventRepository.findTop50ByProcessedFalseOrderByCreatedAtAsc()
    }

    @Transactional
    fun markAsProcessed(event: OutboxEvent) {
        event.processed = true
        outboxEventRepository.save(event)
    }
}