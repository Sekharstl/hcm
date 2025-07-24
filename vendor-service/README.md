# vendor-service

This is the Vendor microservice for the HCM platform. It follows the structure and conventions of position-service.

## Build

```
mvn clean install
```

## Run

```
mvn spring-boot:run
```

## API

- Runs on port **9128** by default.
- OpenAPI spec is generated in `src/main/resources/openapi.yaml` after build.
- Endpoints:
  - `GET /vendors` - List all vendors
  - `GET /vendors/{vendorId}` - Get vendor by ID
  - `POST /vendors` - Create a new vendor
  - `DELETE /vendors/{vendorId}` - Delete a vendor
  - `GET /vendors/statuses` - List all vendor statuses
  - `GET /vendors/statuses/{statusId}` - Get vendor status by ID
  - `POST /vendors/statuses` - Create a new vendor status
  - `DELETE /vendors/statuses/{statusId}` - Delete a vendor status

## Kafka

- Kafka consumer configuration is inherited from `hcm-message-broker`.
- Consumers:
  - `CreateVendorConsumer` (listens to `${vendor.kafka.topic}`)
  - `CreateVendorStatusConsumer` (listens to `${vendorstatus.kafka.topic}`)

## Dependencies
- hcm-common
- hcm-message-broker

## Configuration

Configuration is managed via `src/main/resources/application.properties`.

Key properties:
- `server.port=9128` (default port)
- `spring.datasource.url` (PostgreSQL connection)
- `spring.kafka.bootstrap-servers` (Kafka broker)
- `management.tracing.enabled=false` (disable Zipkin tracing by default)
- `vendor.kafka.topic`, `vendor.kafka.group-id`, etc. (Kafka topics and groups)

See the main project README for more details on environment setup and port mapping. 