# Candidate Response Association in HCM Core

This document explains how to listen to response consumers and associate candidates with response content in the hcm-core service.

## Overview

The hcm-core service acts as an orchestrator that publishes candidate creation events and listens to responses from the candidate-service. This document explains the complete flow and how to associate candidates with their response content.

## Architecture Flow

```
1. Client Request → hcm-core → Kafka → candidate-service
2. candidate-service → Process → Kafka Response → hcm-core
3. hcm-core → Store Response → Associate with Candidate
```

## Components

### 1. Event Publishing (CandidateServiceImpl)

The `createCandidate` method publishes events to Kafka:

```java
@Override
public void createCandidate(CandidateCreateDTO candidate) {
    publishEvent("create-candidate", candidate != null ? candidate.getEmail() : null, candidate);
}
```

**Key Points:**
- Uses email as the correlation key
- Publishes to "create-candidate" topic
- Sends the entire candidate data

### 2. Response Consumer (CandidateCreateResponseConsumer)

Listens to responses from candidate-service:

```java
@TopicListener(
    topic = "${candidate.kafka.topic.create.response}",
    groupId = "${candidate.kafka.group-id}",
    valueType = CandidateCreateResponseDTO.class,
    enableProperty = "candidate.kafka.enable"
)
public class CandidateCreateResponseConsumer implements MessageHandler<CandidateCreateResponseDTO> {
    
    private final CandidateResponseStorageService responseStorageService;
    
    @Override
    public void handle(CandidateCreateResponseDTO response, String key) {
        // Store the response status
        responseStorageService.storeCandidateResponse(response);
        
        // Handle success/failure logic
        if (response.isSuccess()) {
            log.info("Candidate creation successful: {}", response.getCandidateId());
        } else {
            log.warn("Candidate creation failed: {}", response.getMessage());
        }
    }
}
```

### 3. Response Storage (CandidateResponseStorageService)

Manages response status storage with in-memory cache:

```java
@Service
public class CandidateResponseStorageService {
    
    private final Map<String, CandidateCreateResponseDTO> responseCache = new ConcurrentHashMap<>();
    private final Map<String, CandidateCreateResponseDTO> emailResponseCache = new ConcurrentHashMap<>();
    
    public void storeCandidateResponse(CandidateCreateResponseDTO response) {
        if (response.getCandidateId() != null) {
            responseCache.put(response.getCandidateId().toString(), response);
        }
        if (response.getEmail() != null) {
            emailResponseCache.put(response.getEmail(), response);
        }
    }
}
```

## Response Association Methods

### 1. By Candidate ID

```java
// Get response by candidate ID
CandidateCreateResponseDTO response = responseStorageService.getResponseByCandidateId("candidate-uuid");

// Check if response exists
boolean exists = responseStorageService.hasResponse("candidate-uuid");
```

### 2. By Email

```java
// Get response by email (correlation key)
CandidateCreateResponseDTO response = responseStorageService.getResponseByEmail("candidate@example.com");

// Check if response exists
boolean exists = responseStorageService.hasResponseByEmail("candidate@example.com");
```

### 3. Bulk Operations

```java
// Get all responses
List<CandidateCreateResponseDTO> allResponses = responseStorageService.getAllResponses();

// Get pending responses
List<CandidateCreateResponseDTO> pendingResponses = responseStorageService.getPendingResponses();

// Get successful responses
List<CandidateCreateResponseDTO> successfulResponses = responseStorageService.getSuccessfulResponses();

// Get failed responses
List<CandidateCreateResponseDTO> failedResponses = responseStorageService.getFailedResponses();
```

## API Endpoints

The `CandidateResponseController` provides REST endpoints for querying response statuses:

### Get Response by Candidate ID
```
GET /api/v1/candidate-responses/{candidateId}
```

### Get Response by Email
```
GET /api/v1/candidate-responses/email/{email}
```

### Get All Responses
```
GET /api/v1/candidate-responses
```

### Get Pending Responses
```
GET /api/v1/candidate-responses/pending
```

### Get Successful Responses
```
GET /api/v1/candidate-responses/successful
```

### Get Failed Responses
```
GET /api/v1/candidate-responses/failed
```

### Check Response Exists
```
GET /api/v1/candidate-responses/{candidateId}/exists
GET /api/v1/candidate-responses/email/{email}/exists
```

### Delete Response
```
DELETE /api/v1/candidate-responses/{candidateId}
```

## Response DTO Structure

```java
public class CandidateCreateResponseDTO {
    private UUID candidateId;      // Generated candidate ID
    private String status;         // CREATED, FAILED, PROCESSING
    private String message;        // Success/error message
    private boolean success;       // Operation success flag
    private String email;          // Correlation key
}
```

## Configuration

### Kafka Topics
```properties
# Candidate creation request topic
candidate.kafka.topic=create-candidate

# Candidate creation response topic
candidate.kafka.topic.create.response=candidate.create.response

# Consumer group
candidate.kafka.group-id=hcm-core-candidate-group

# Enable/disable
candidate.kafka.enable=true
```

## Usage Examples

### 1. Create Candidate and Track Response

```java
// 1. Create candidate (publishes event)
candidateService.createCandidate(candidateCreateDTO);

// 2. Poll for response status
CandidateCreateResponseDTO response = candidateService.getCandidateResponseStatusByEmail("candidate@example.com");

if (response != null) {
    if (response.isSuccess()) {
        log.info("Candidate created with ID: {}", response.getCandidateId());
    } else {
        log.error("Creation failed: {}", response.getMessage());
    }
}
```

### 2. Monitor All Responses

```java
// Get all pending responses
List<CandidateCreateResponseDTO> pending = responseStorageService.getPendingResponses();

// Get all successful responses
List<CandidateCreateResponseDTO> successful = responseStorageService.getSuccessfulResponses();

// Get all failed responses
List<CandidateCreateResponseDTO> failed = responseStorageService.getFailedResponses();
```

### 3. Clean Up Responses

```java
// Delete response after processing
responseStorageService.deleteResponse("candidate-uuid");
```

## Error Handling

### 1. Response Not Found
```java
CandidateCreateResponseDTO response = responseStorageService.getResponseByCandidateId("non-existent-id");
if (response == null) {
    // Handle case where response doesn't exist
    log.warn("No response found for candidate ID: {}", candidateId);
}
```

### 2. Consumer Error Handling
```java
@Override
public void handle(CandidateCreateResponseDTO response, String key) {
    try {
        responseStorageService.storeCandidateResponse(response);
        // Process response
    } catch (Exception e) {
        log.error("Error processing candidate response: {}", e.getMessage(), e);
        // Implement retry logic or error notification
    }
}
```

## Best Practices

### 1. Correlation Keys
- Always use email as the correlation key for consistency
- Store responses by both candidate ID and email for flexibility

### 2. Response Cleanup
- Implement TTL (Time To Live) for responses
- Clean up old responses periodically
- Consider using Redis or database for persistence

### 3. Monitoring
- Log all response events for debugging
- Monitor response processing times
- Set up alerts for failed responses

### 4. Scalability
- Consider using Redis for distributed caching
- Implement response partitioning for high volume
- Add response compression for large payloads

## Alternative Storage Options

### 1. Redis Storage
```java
@Service
public class RedisCandidateResponseStorageService {
    private final RedisTemplate<String, CandidateCreateResponseDTO> redisTemplate;
    
    public void storeCandidateResponse(CandidateCreateResponseDTO response) {
        String key = "candidate:response:" + response.getCandidateId();
        redisTemplate.opsForValue().set(key, response, Duration.ofHours(24));
    }
}
```

### 2. Database Storage
```java
@Entity
public class CandidateResponseEntity {
    @Id
    private String candidateId;
    private String email;
    private String status;
    private String message;
    private boolean success;
    private LocalDateTime createdAt;
}
```

## Testing

### Unit Test Example
```java
@Test
void testCandidateResponseAssociation() {
    // Given
    CandidateCreateResponseDTO response = CandidateCreateResponseDTO.builder()
        .candidateId(UUID.randomUUID())
        .email("test@example.com")
        .success(true)
        .status("CREATED")
        .message("Candidate created successfully")
        .build();
    
    // When
    responseStorageService.storeCandidateResponse(response);
    
    // Then
    CandidateCreateResponseDTO retrieved = responseStorageService.getResponseByEmail("test@example.com");
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.isSuccess()).isTrue();
    assertThat(retrieved.getStatus()).isEqualTo("CREATED");
}
```

This implementation provides a complete solution for associating candidates with their response content in the hcm-core service, with proper storage, retrieval, and monitoring capabilities. 