# Application Service

The Application Service is a Spring Boot microservice responsible for managing job applications and application statuses in the HCM (Human Capital Management) system.

## Overview

This service handles:
- Job applications submitted by candidates
- Application status management
- Integration with other HCM services via Kafka messaging

## Features

### Application Management
- Create, read, update, and delete job applications
- Query applications by candidate ID or requisition ID
- Track application status changes
- Validate application data

### Application Status Management
- Manage application statuses (e.g., Applied, Under Review, Interviewed, Hired, Rejected)
- Create, read, update, and delete application statuses
- Support for custom status definitions

### Kafka Integration
- Consume application creation events
- Consume application update events
- Consume application deletion events
- Consume application status management events

## Technology Stack

- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL**
- **Apache Kafka**
- **ModelMapper**
- **Lombok**
- **SpringDoc OpenAPI**

## API Endpoints

### Applications

- `GET /api/v1/applications/{applicationId}` - Get application by ID
- `GET /api/v1/applications` - Get all applications
- `GET /api/v1/applications/candidate/{candidateId}` - Get applications by candidate
- `GET /api/v1/applications/requisition/{requisitionId}` - Get applications by requisition

### Application Statuses

- `GET /api/v1/application-statuses/{statusId}` - Get application status by ID
- `GET /api/v1/application-statuses` - Get all application statuses

**Note:** Create, Update, and Delete operations are handled via Kafka events, not REST endpoints. The frontend communicates with hcm-core (API Gateway) which publishes events to Kafka for these operations.

## Kafka Topics

### Application Topics
- `create-application` - Application creation events
- `update-application` - Application update events
- `delete-application` - Application deletion events

### Application Status Topics
- `create-application-status` - Application status creation events
- `update-application-status` - Application status update events
- `delete-application-status` - Application status deletion events

## Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://10.19.5.50:30001/stl_hcm_db
spring.datasource.username=postgres
spring.datasource.password=Sterlite@2025
spring.jpa.properties.hibernate.default_schema=hcm
```

### Kafka Configuration
```properties
application.kafka.enable=true
application.kafka.group-id=application-consumer-group
application.kafka.topic=create-application
application.kafka.topic.update=update-application
application.kafka.topic.delete=delete-application

applicationstatus.kafka.enable=true
applicationstatus.kafka.group-id=application-consumer-group
applicationstatus.kafka.topic=create-application-status
applicationstatus.kafka.topic.update=update-application-status
applicationstatus.kafka.topic.delete=delete-application-status
```

## Running the Service

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL database
- Apache Kafka

### Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Docker
```bash
# Build Docker image
docker build -t application-service .

# Run Docker container
docker run -p 9128:9128 application-service
```

## Development

### Project Structure
```
src/main/java/tech/stl/hcm/application/
├── ApplicationServiceApplication.java
├── controller/
│   ├── ApplicationController.java
│   └── ApplicationStatusController.java
├── service/
│   ├── ApplicationService.java
│   ├── ApplicationServiceImpl.java
│   ├── ApplicationStatusService.java
│   └── ApplicationStatusServiceImpl.java
├── config/
│   └── ModelMapperConfig.java
└── exception/
    ├── ApplicationNotFoundException.java
    └── ApplicationStatusNotFoundException.java

src/main/java/tech/stl/hcm/consumers/
├── CreateApplicationConsumer.java
├── UpdateApplicationConsumer.java
├── DeleteApplicationConsumer.java
├── CreateApplicationStatusConsumer.java
├── UpdateApplicationStatusConsumer.java
└── DeleteApplicationStatusConsumer.java
```

### Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

## Monitoring and Health Checks

The service exposes the following endpoints for monitoring:
- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

## Security

The service includes basic security configuration with:
- Spring Security
- Rate limiting
- Circuit breaker pattern
- Request validation

## Dependencies

### Internal Dependencies
- `hcm-common` - Shared entities, DTOs, and repositories
- `hcm-message-broker` - Kafka configuration and message handling

### External Dependencies
- Spring Boot Starters (Web, Data JPA, Actuator, Security, Validation)
- PostgreSQL driver
- Apache Kafka
- ModelMapper
- Lombok
- SpringDoc OpenAPI

## Contributing

1. Follow the existing code style and patterns
2. Add unit tests for new functionality
3. Update documentation as needed
4. Ensure all tests pass before submitting

## License

Copyright STLDIGITAL 2025 