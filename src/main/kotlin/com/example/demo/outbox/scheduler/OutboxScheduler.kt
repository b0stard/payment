package com.example.demo.outbox.scheduler

import com.example.demo.outbox.service.OutboxService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxScheduler(
    private val outboxService: OutboxService
) {
    private val log = LoggerFactory.getLogger(javaClass)
    @Scheduled(fixedDelay = 5000)
    fun processEvents() {

        val events = outboxService.getPendingEvents()

        events.forEach { event ->

            log.info(
                "Processing outbox event: type={}, aggregateId={}, payload={}",
                event.eventType,
                event.aggregateId,
                event.payload
            )

            outboxService.markAsProcessed(event)
        }
    }
}