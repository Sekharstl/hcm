# HCM Core Service - Complete OpenAPI Specification

## Overview
The HCM Core service OpenAPI specification (`hcm-core.json`) has been fully completed with all endpoints, schemas, and features. This document provides a comprehensive summary of what has been implemented.

## Service Information
- **Title**: HCM Core Service API
- **Version**: 1.0.0
- **Description**: Core service for Human Capital Management system providing master data management, candidate operations, position management, and application handling.
- **Contact**: HCM Development Team (hcm-dev@stl.tech)
- **License**: MIT

## Security
- **Type**: Bearer Authentication (JWT)
- **Scheme**: http
- **Bearer Format**: JWT

## Servers
- **Local Development**: http://localhost:8080
- **Production**: https://hcm-core.stl.tech

## Complete Endpoint Coverage

### 1. Master Data Endpoints (with Autocomplete Support)
All master data endpoints include both standard CRUD operations and autocomplete search functionality:

#### Document Types
- `GET /master-data/document-types` - Get all document types
- `GET /master-data/document-types/{id}` - Get document type by ID
- `GET /master-data/document-types/search` - Search document types (autocomplete)

#### ID Types
- `GET /master-data/id-types` - Get all ID types
- `GET /master-data/id-types/{id}` - Get ID type by ID
- `GET /master-data/id-types/search` - Search ID types (autocomplete)

#### Countries
- `GET /master-data/countries` - Get all countries
- `GET /master-data/countries/{id}` - Get country by ID
- `GET /master-data/countries/code/{code}` - Get country by code
- `GET /master-data/countries/search` - Search countries (autocomplete)

#### States
- `GET /master-data/states` - Get all states
- `GET /master-data/states/search` - Search states (autocomplete)

#### Skills
- `GET /master-data/skills` - Get all skills
- `GET /master-data/skills/search` - Search skills (autocomplete)

#### Departments
- `GET /master-data/departments` - Get all departments
- `GET /master-data/departments/search` - Search departments (autocomplete)

#### User Roles
- `GET /master-data/user-roles` - Get all user roles
- `GET /master-data/user-roles/search` - Search user roles (autocomplete)

#### User Types
- `GET /master-data/user-types` - Get all user types
- `GET /master-data/user-types/search` - Search user types (autocomplete)

### 2. Candidate Management (with Pagination)
Complete candidate lifecycle management with pagination support:

#### Main Candidate Operations
- `GET /candidates` - Get all candidates (paginated)
- `POST /candidates` - Create candidate
- `GET /candidates/{candidateId}` - Get candidate by ID
- `PUT /candidates/{candidateId}` - Update candidate
- `DELETE /candidates/{candidateId}` - Delete candidate

#### Candidate Skills
- `GET /candidates/{candidateId}/skills` - Get candidate skills
- `POST /candidates/{candidateId}/skills` - Add candidate skill

#### Candidate Education
- `GET /candidates/{candidateId}/educations` - Get candidate educations
- `POST /candidates/{candidateId}/educations` - Add candidate education

#### Candidate Work History
- `GET /candidates/{candidateId}/work-histories` - Get candidate work histories
- `POST /candidates/{candidateId}/work-histories` - Add candidate work history

#### Candidate Certifications
- `GET /candidates/{candidateId}/certifications` - Get candidate certifications
- `POST /candidates/{candidateId}/certifications` - Add candidate certification

#### Candidate References
- `GET /candidates/{candidateId}/references` - Get candidate references
- `POST /candidates/{candidateId}/references` - Add candidate reference

#### Candidate Identities
- `GET /candidates/{candidateId}/identities` - Get candidate identities
- `POST /candidates/{candidateId}/identities` - Add candidate identity

#### Candidate Documents
- `GET /candidates/{candidateId}/documents` - Get candidate documents
- `POST /candidates/{candidateId}/documents` - Add candidate document

### 3. Position Management (with Pagination)
Complete position and position status management:

#### Positions
- `GET /positions` - Get all positions (paginated)
- `POST /positions` - Create position
- `GET /positions/{id}` - Get position by ID
- `PUT /positions/{id}` - Update position
- `DELETE /positions/{id}` - Delete position

#### Position Statuses
- `GET /positions/statuses` - Get all position statuses
- `POST /positions/statuses` - Create position status
- `GET /positions/statuses/{id}` - Get position status by ID
- `PUT /positions/statuses/{id}` - Update position status
- `DELETE /positions/statuses/{id}` - Delete position status

### 4. Application Management (with Pagination)
Complete application lifecycle management:

#### Applications
- `GET /applications` - Get all applications (paginated)
- `POST /applications` - Create application
- `GET /applications/candidate/{candidateId}` - Get applications for candidate
- `GET /applications/candidate/{candidateId}/application/{applicationId}` - Get application by ID
- `PUT /applications/candidate/{candidateId}/application/{applicationId}` - Update application
- `DELETE /applications/candidate/{candidateId}/application/{applicationId}` - Delete application

#### Application Status
- `GET /applications/candidate/{candidateId}/application/{applicationId}/status` - Get application status
- `PUT /applications/candidate/{candidateId}/application/{applicationId}/status` - Update application status
- `DELETE /applications/candidate/{candidateId}/application/{applicationId}/status` - Delete application status
- `POST /applications/status` - Create application status

### 5. Job Requisition Management (with Pagination)
Complete job requisition lifecycle:

- `GET /job-requisitions` - Get all job requisitions (paginated)
- `POST /job-requisitions` - Create job requisition
- `PUT /job-requisitions` - Update job requisition
- `GET /job-requisitions/{id}` - Get job requisition by ID
- `DELETE /job-requisitions/{id}` - Delete job requisition

### 6. Organization Management (with Pagination)
Complete organization lifecycle:

- `GET /organizations` - Get all organizations (paginated)
- `POST /organizations` - Create organization
- `GET /organizations/{organizationId}` - Get organization by ID
- `PUT /organizations/{organizationId}` - Update organization
- `DELETE /organizations/{organizationId}` - Delete organization

### 7. Vendor Management (with Pagination)
Complete vendor lifecycle:

- `GET /vendors` - Get all vendors (paginated)
- `POST /vendors` - Create vendor
- `GET /vendors/{vendorId}` - Get vendor by ID
- `PUT /vendors/{vendorId}` - Update vendor
- `DELETE /vendors/{vendorId}` - Delete vendor

### 8. Tenant Management (with Pagination)
Complete tenant lifecycle:

- `GET /tenants` - Get all tenants (paginated)
- `POST /tenants` - Create tenant
- `GET /tenants/{tenantId}` - Get tenant by ID
- `PUT /tenants/{tenantId}` - Update tenant
- `DELETE /tenants/{tenantId}` - Delete tenant

### 9. Core Service Endpoints
Basic service functionality:

- `GET /hello` - Hello endpoint for service verification
- `POST /publish` - Publish message to Kafka topic

## Key Features Implemented

### 1. Pagination Support
All list endpoints include comprehensive pagination parameters:
- `page` - Page number (0-based)
- `size` - Page size (default: 20)
- `sortBy` - Sort field
- `sortDirection` - Sort direction (ASC/DESC)

### 2. Autocomplete Support
All master data endpoints include search functionality with:
- `query` - Search query parameter
- `limit` - Maximum results (default: 10)

### 3. Comprehensive Error Handling
All endpoints include proper error responses:
- `400` - Bad Request
- `404` - Not Found
- `500` - Internal Server Error

### 4. Standardized Response Formats
- Paginated responses use `PaginatedResponse` schema
- Error responses use `Error` schema
- All responses include proper content types

### 5. Complete Schema Definitions
All DTOs and entities are properly defined with:
- Data types and formats
- Required/optional fields
- Descriptions for all properties
- Proper references and relationships

## Schema Coverage

### Core Schemas
- `Error` - Standard error response
- `PaginatedResponse` - Standard paginated response

### Master Data Schemas
- `DocumentTypeDTO`
- `IdTypeDTO`
- `CountryDTO`
- `StateDTO`
- `SkillDTO`
- `DepartmentDTO`
- `UserRoleDTO`
- `UserTypeDTO`

### Business Entity Schemas
- `CandidateDTO` and related DTOs
- `PositionDTO` and `PositionStatusDTO`
- `ApplicationDTO` and `ApplicationStatusDTO`
- `JobRequisitionDTO`
- `OrganizationDTO`
- `VendorDTO`
- `TenantDTO`

### Create DTOs
- All entities have corresponding Create DTOs for POST operations

## API Tags Organization
Endpoints are organized into logical tags:
- **Master Data** - All master data operations
- **Candidates** - Candidate management
- **Positions** - Position and status management
- **Applications** - Application lifecycle
- **Job Requisitions** - Job requisition management
- **Organizations** - Organization management
- **Vendors** - Vendor management
- **Tenants** - Tenant management
- **Core** - Basic service operations

## Usage Examples

### Pagination Example
```bash
GET /candidates?page=0&size=10&sortBy=firstName&sortDirection=ASC
```

### Autocomplete Example
```bash
GET /master-data/skills/search?query=java&limit=5
```

### Create Candidate Example
```bash
POST /candidates
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

## Next Steps
The OpenAPI specification is now complete and ready for:
1. Frontend integration
2. API documentation generation
3. Client code generation
4. Testing and validation
5. Deployment and monitoring

## File Location
- **OpenAPI Specification**: `hcm-core/src/main/resources/hcm-core.json`
- **Summary Document**: `HCM_CORE_OPENAPI_COMPLETION_SUMMARY.md`

The HCM Core service now has a comprehensive, production-ready OpenAPI specification that covers all endpoints with proper pagination, autocomplete support, and standardized error handling. 