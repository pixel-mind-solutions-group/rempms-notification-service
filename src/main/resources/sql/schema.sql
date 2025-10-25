CREATE TABLE IF NOT EXISTS email_log (
    id SERIAL PRIMARY KEY,
    payload TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP,
    created_by VARCHAR(100),
    retry_at TIMESTAMP,
    retry_count INT DEFAULT 0,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS email_has_attachment (
    id SERIAL PRIMARY KEY,
    original_file_name VARCHAR(255),
    attachment_base64 TEXT,
    content_type VARCHAR(100),
    email_log_id INT REFERENCES email_log(id)
);

CREATE TABLE IF NOT EXISTS error_log (
    id SERIAL PRIMARY KEY,
    error_message TEXT,
    payload TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS application_source (
    id SERIAL PRIMARY KEY,
    source VARCHAR(100)
);