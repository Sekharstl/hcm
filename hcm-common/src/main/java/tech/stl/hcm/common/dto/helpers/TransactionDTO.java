package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private UUID transactionId;
    private String serviceName;
    private String operationType; // CREATE, UPDATE, DELETE
    private String topicName;
    private String status; // QUEUED, PENDING, PROCESSING, SUCCESS, FAILED
    private String entityType; // CANDIDATE, VENDOR, EMPLOYEE, etc.
    private UUID entityId; // Primary key of the created/updated/deleted entity
    private String correlationKey; // Email, ID, or other correlation key
    private String requestPayload; // Original request payload (JSON)
    private String responsePayload; // Response payload (JSON)
    private String errorMessage; // Error message if failed
    private Integer retryCount;
    private Integer maxRetries;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant processedAt; // When the operation was completed
    private UUID createdBy;
    private UUID updatedBy;
} 