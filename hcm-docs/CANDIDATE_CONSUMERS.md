# Candidate Response Consumers in HCM Core

This document describes the Kafka consumers implemented in the HCM Core service to handle candidate operation responses.

## Overview

The HCM Core service includes consumers that listen to candidate operation response topics from the Candidate Service. These consumers handle responses for create, update, and delete operations.

## Consumers

### 1. CandidateCreateResponseConsumer

**Topic:** `candidate.create.response`  
**Message Type:** `CandidateCreateResponseDTO`  
**Purpose:** Handles responses from candidate creation operations

**Configuration:**
```properties
candidate.kafka.topic.create.response=candidate.create.response
candidate.kafka.group-id=hcm-core-candidate-response-group
candidate.kafka.enable=true
```

**Response Structure:**
```java
public class CandidateCreateResponseDTO {
    private UUID candidateId;
    private String status;
    private String message;
    private boolean success;
    private String email;
}
```

### 2. CandidateUpdateResponseConsumer

**Topic:** `candidate.update.response`  
**Message Type:** `Map<String, Object>`  
**Purpose:** Handles responses from candidate update operations

**Configuration:**
```properties
candidate.kafka.topic.update.response=candidate.update.response
```

**Response Structure:**
```json
{
  "candidateId": "uuid",
  "email": "candidate@example.com",
  "success": true,
  "status": "UPDATED",
  "message": "Candidate updated successfully",
  "timestamp": 1234567890
}
```

### 3. CandidateDeleteResponseConsumer

**Topic:** `candidate.delete.response`  
**Message Type:** `Map<String, Object>`  
**Purpose:** Handles responses from candidate delete operations

**Configuration:**
```properties
candidate.kafka.topic.delete.response=candidate.delete.response
```

**Response Structure:**
```json
{
  "candidateId": "uuid",
  "email": "candidate@example.com",
  "success": true,
  "status": "DELETED",
  "message": "Candidate deleted successfully",
  "timestamp": 1234567890
}
```

## Implementation Details

### Consumer Pattern

All consumers follow the same pattern:
1. Use `@TopicListener` annotation for configuration
2. Implement `MessageHandler<T>` interface
3. Use MDC for logging context
4. Handle both success and failure scenarios
5. Log appropriate messages based on response status

### Error Handling

- Consumers log warnings for failed operations
- Null values are handled gracefully
- MDC context is cleared in finally blocks
- No exceptions are thrown to prevent consumer failures

### Logging

Each consumer logs:
- Operation type (create/update/delete)
- Email for correlation
- Success/failure status
- Response message
- Candidate ID (when available)

## Testing

Unit tests are provided for each consumer:
- `CandidateCreateResponseConsumerTest`
- Tests cover successful, failed, and edge cases
- Mock dependencies to isolate consumer logic

## Future Enhancements

The consumers are designed to be extensible. Potential enhancements include:
- Integration with notification services
- UI update mechanisms
- Retry logic for failed operations
- Metrics collection
- Audit trail logging

## Dependencies

- `hcm-message-broker`: For Kafka consumer functionality
- `hcm-common`: For DTOs and common utilities
- Spring Boot: For dependency injection and configuration 