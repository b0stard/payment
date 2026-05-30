CREATE TABLE outbox_events (
                               id UUID PRIMARY KEY,
                               aggregate_id UUID NOT NULL,
                               aggregate_type VARCHAR(100) NOT NULL,
                               event_type VARCHAR(100) NOT NULL,
                               payload TEXT NOT NULL,
                               processed BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               processed_at TIMESTAMP NULL
);

CREATE INDEX idx_outbox_processed_created
    ON outbox_events(processed, created_at);