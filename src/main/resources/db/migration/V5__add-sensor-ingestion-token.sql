ALTER TABLE sensors
    ADD COLUMN ingestion_token_hash VARCHAR(64);

ALTER TABLE sensors
    ADD COLUMN last_seen_at TIMESTAMPTZ;

CREATE UNIQUE INDEX uk_sensors_ingestion_token_hash
    ON sensors (ingestion_token_hash)
    WHERE ingestion_token_hash IS NOT NULL;