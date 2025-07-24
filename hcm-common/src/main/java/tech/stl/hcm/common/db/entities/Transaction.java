package tech.stl.hcm.common.db.entities;

import lombok.*;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transaction", schema = "hcm")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private UUID transactionId;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType; // CREATE, UPDATE, DELETE

    @Column(name = "topic_name", nullable = false, length = 200)
    private String topicName;

    @Column(name = "status", nullable = false, length = 50)
    private String status; // QUEUED, PENDING, PROCESSING, SUCCESS, FAILED

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType; // CANDIDATE, VENDOR, EMPLOYEE, etc.

    @Column(name = "entity_id")
    private UUID entityId; // Primary key of the created/updated/deleted entity

    @Column(name = "correlation_key", length = 255)
    private String correlationKey; // Email, ID, or other correlation key

    @Column(name = "request_payload", columnDefinition = "TEXT")
    private String requestPayload; // Original request payload (JSON)

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload; // Response payload (JSON)

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage; // Error message if failed

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "processed_at")
    private Instant processedAt; // When the operation was completed

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;
} 