-- Create transaction table for tracking operations
CREATE TABLE IF NOT EXISTS hcm.transaction (
    transaction_id UUID PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL,
    operation_type VARCHAR(50) NOT NULL, -- CREATE, UPDATE, DELETE
    topic_name VARCHAR(200) NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, SUCCESS, FAILED, PROCESSING
    entity_type VARCHAR(100) NOT NULL, -- CANDIDATE, VENDOR, EMPLOYEE, etc.
    entity_id UUID, -- Primary key of the created/updated/deleted entity
    correlation_key VARCHAR(255), -- Email, ID, or other correlation key
    request_payload TEXT, -- Original request payload (JSON)
    response_payload TEXT, -- Response payload (JSON)
    error_message TEXT, -- Error message if failed
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 3,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    processed_at TIMESTAMPTZ, -- When the operation was completed
    created_by UUID NOT NULL,
    updated_by UUID NOT NULL
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_transaction_status ON hcm.transaction(status);
CREATE INDEX IF NOT EXISTS idx_transaction_service ON hcm.transaction(service_name);
CREATE INDEX IF NOT EXISTS idx_transaction_entity_type ON hcm.transaction(entity_type);
CREATE INDEX IF NOT EXISTS idx_transaction_correlation_key ON hcm.transaction(correlation_key);
CREATE INDEX IF NOT EXISTS idx_transaction_created_at ON hcm.transaction(created_at);
CREATE INDEX IF NOT EXISTS idx_transaction_entity_id ON hcm.transaction(entity_id);

-- Create a view for pending transactions
CREATE VIEW hcm.pending_transactions AS
SELECT * FROM hcm.transaction 
WHERE status IN ('PENDING', 'PROCESSING')
ORDER BY created_at ASC;

-- Create a view for failed transactions
CREATE VIEW hcm.failed_transactions AS
SELECT * FROM hcm.transaction 
WHERE status = 'FAILED'
ORDER BY created_at DESC;

-- Create a view for successful transactions
CREATE VIEW hcm.successful_transactions AS
SELECT * FROM hcm.transaction 
WHERE status = 'SUCCESS'
ORDER BY created_at DESC; 