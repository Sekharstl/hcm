# Transaction Management System

## Overview

The HCM platform implements a robust transaction management system that tracks the status of asynchronous operations across all services. This system ensures reliable tracking of create, update, and delete operations for all entities in the platform.

## Architecture

### Core Components

1. **HCM Core** - Central transaction management
   - Generates transaction IDs
   - Stores transaction records
   - Provides transaction status APIs
   - Publishes Kafka messages with transaction ID wrappers

2. **HCM Common** - Shared transaction components
   - `BaseTransactionConsumer` - Base class for all consumers
   - `TransactionUpdateService` - Service for updating transaction statuses

3. **Individual Services** - Entity-specific operations
   - All services now use the common transaction pattern
   - Consumers extend `BaseTransactionConsumer`
   - Services generate transaction IDs and publish wrapped messages

## Services Using Transaction Pattern

All services in the HCM platform now use the unified transaction management pattern:

### 1. Candidate Service
- **Consumers**: `CreateCandidateConsumer`, `UpdateCandidateConsumer`, `DeleteCandidateConsumer`
- **Entity Type**: `CandidateCreateDTO`, `CandidateDTO`
- **ID Type**: UUID (candidateId)

### 2. Vendor Service
- **Consumers**: `CreateVendorConsumer`, `UpdateVendorConsumer`, `DeleteVendorConsumer`
- **Entity Type**: `VendorCreateDTO`, `VendorDTO`
- **ID Type**: UUID (vendorId)

### 3. Application Service
- **Consumers**: `CreateApplicationConsumer`, `UpdateApplicationConsumer`, `DeleteApplicationConsumer`
- **Entity Type**: `ApplicationCreateDTO`, `ApplicationDTO`
- **ID Type**: Integer (applicationId) - tracked without entity ID

### 4. Tenant Service
- **Consumers**: `CreateTenantConsumer`, `UpdateTenantConsumer`, `DeleteTenantConsumer`
- **Entity Type**: `TenantCreateDTO`, `TenantDTO`
- **ID Type**: UUID (tenantId)

### 5. Organization Service
- **Consumers**: `CreateOrganizationConsumer`, `UpdateOrganizationConsumer`, `DeleteOrganizationConsumer`
- **Entity Type**: `OrganizationCreateDTO`, `OrganizationDTO`
- **ID Type**: UUID (organizationId)

### 6. Job Requisition Service
- **Consumers**: `CreateJobRequisitionConsumer`, `UpdateJobRequisitionConsumer`, `DeleteJobRequisitionConsumer`
- **Entity Type**: `JobRequisitionCreateDTO`, `JobRequisitionDTO`
- **ID Type**: Integer (jobRequisitionId) - tracked without entity ID

### 7. Position Service
- **Consumers**: `CreatePositionConsumer`, `UpdatePositionConsumer`, `DeletePositionConsumer`
- **Entity Type**: `PositionCreateDTO`, `PositionDTO`
- **ID Type**: Integer (positionId) - tracked without entity ID

## Transaction Flow

### 1. Service Layer (HCM Core)
```java
// Generate transaction ID
UUID transactionId = UUID.randomUUID();

// Create transaction record
Transaction transaction = Transaction.builder()
    .transactionId(transactionId)
    .entityType("Candidate")
    .operation("CREATE")
    .status(TransactionStatus.PENDING)
    .createdAt(Instant.now())
    .build();
transactionRepository.save(transaction);

// Publish Kafka message with transaction wrapper
TransactionWrapper<CandidateCreateDTO> wrapper = TransactionWrapper.<CandidateCreateDTO>builder()
    .transactionId(transactionId)
    .entity(candidateCreateDTO)
    .build();
kafkaTemplate.send("create-candidate", wrapper);
```

### 2. Consumer Layer (Individual Services)
```java
@Component
@TopicListener(topic = "${candidate.kafka.topic}", valueType = java.util.Map.class)
public class CreateCandidateConsumer extends BaseTransactionConsumer<CandidateCreateDTO> {
    
    private final CandidateService candidateService;

    public CreateCandidateConsumer(ObjectMapper objectMapper, 
                                 TransactionUpdateService transactionUpdateService, 
                                 CandidateService candidateService) {
        super(objectMapper, transactionUpdateService);
        this.candidateService = candidateService;
    }

    @Override
    protected Class<CandidateCreateDTO> getEntityType() {
        return CandidateCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Candidate";
    }

    @Override
    protected Object processEntity(CandidateCreateDTO candidate, UUID transactionId) {
        return candidateService.createCandidate(candidate);
    }

    @Override
    protected boolean isSuccess(Object result) {
        if (result instanceof CandidateDTO) {
            CandidateDTO candidate = (CandidateDTO) result;
            return candidate != null && candidate.getCandidateId() != null;
        }
        return false;
    }

    @Override
    protected UUID getEntityId(Object result) {
        if (result instanceof CandidateDTO) {
            CandidateDTO candidate = (CandidateDTO) result;
            return candidate.getCandidateId();
        }
        return null;
    }
}
```

## Transaction Status API

### Get Transaction Status
```http
GET /api/v1/transactions/{transactionId}
```

**Response:**
```json
{
  "transactionId": "550e8400-e29b-41d4-a716-446655440000",
  "entityType": "Candidate",
  "operation": "CREATE",
  "status": "COMPLETED",
  "entityId": "550e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2024-01-01T10:00:00Z",
  "updatedAt": "2024-01-01T10:00:05Z"
}
```

### Get All Transactions
```http
GET /api/v1/transactions?page=0&size=20&sort=createdAt,desc
```

### Get Transactions by Entity
```http
GET /api/v1/transactions/entity/{entityId}?page=0&size=20
```

## Database Schema

### Transaction Table
```sql
CREATE TABLE transactions (
    transaction_id UUID PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    operation VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    entity_id UUID,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_entity_id ON transactions(entity_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
```

## Transaction Statuses

- **QUEUED** - Transaction created and published to Kafka, waiting to be consumed
- **PENDING** - Transaction consumed from Kafka, ready for processing
- **PROCESSING** - Transaction currently being processed by the target service
- **SUCCESS** - Transaction completed successfully
- **FAILED** - Transaction failed with error

## Transaction State Flow

```
QUEUED → PENDING → PROCESSING → SUCCESS
                ↓
              FAILED
```

## Error Handling

### Consumer Error Handling
```java
@Override
protected void handleError(Exception e, T entity, UUID transactionId) {
    log.error("Error processing {} transaction {}: {}", 
              getEntityName(), transactionId, e.getMessage());
    
    // Update transaction status to FAILED
    transactionUpdateService.updateTransactionStatus(
        transactionId, 
        TransactionStatus.FAILED, 
        e.getMessage()
    );
}
```

### Service Error Handling
```java
try {
    // Process entity
    Object result = processEntity(entity, transactionId);
    
    if (isSuccess(result)) {
        // Update transaction status to COMPLETED
        transactionUpdateService.updateTransactionStatus(
            transactionId, 
            TransactionStatus.COMPLETED, 
            getEntityId(result)
        );
    } else {
        // Update transaction status to FAILED
        transactionUpdateService.updateTransactionStatus(
            transactionId, 
            TransactionStatus.FAILED, 
            "Processing failed"
        );
    }
} catch (Exception e) {
    handleError(e, entity, transactionId);
}
```

## Benefits

1. **Reliability** - All operations are tracked and can be monitored
2. **Consistency** - Unified pattern across all services
3. **Observability** - Complete audit trail of all operations
4. **Error Recovery** - Failed transactions can be identified and retried
5. **Code Reuse** - Common base classes reduce duplication
6. **Maintainability** - Centralized transaction management

## Migration Notes

- All consumers now extend `BaseTransactionConsumer`
- All services publish Kafka messages with transaction ID wrappers
- Transaction status updates are handled via REST calls to HCM Core
- Entity IDs with Integer type are tracked without entity ID in transaction records
- All services use the same error handling and logging patterns 