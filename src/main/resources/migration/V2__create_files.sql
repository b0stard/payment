CREATE TABLE files (
                       id UUID PRIMARY KEY,
                       owner_id UUID NOT NULL,
                       original_file_name VARCHAR(255) NOT NULL,
                       storage_key VARCHAR(500) NOT NULL UNIQUE,
                       content_type VARCHAR(255) NOT NULL,
                       size BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP NULL,

                       CONSTRAINT fk_files_owner
                           FOREIGN KEY (owner_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE
);

CREATE INDEX idx_files_owner_id ON files(owner_id);
CREATE INDEX idx_files_owner_deleted ON files(owner_id, deleted_at);