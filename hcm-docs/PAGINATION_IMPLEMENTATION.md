# Pagination Implementation for HCM Core Service

## Overview

This document describes the pagination implementation that has been added to the getAll endpoints in the hcm-core service. The implementation provides consistent pagination across all major entities in the HCM system.

## What Was Implemented

### 1. Pagination DTOs

Created two new DTOs in `hcm-common`:

- **`PaginatedResponseDTO<T>`**: A generic wrapper that contains:
  - `content`: List of items for the current page
  - `pageNumber`: Current page number (0-based)
  - `pageSize`: Number of items per page
  - `totalElements`: Total number of items across all pages
  - `totalPages`: Total number of pages
  - `hasNext`: Whether there's a next page
  - `hasPrevious`: Whether there's a previous page
  - `isFirst`: Whether this is the first page
  - `isLast`: Whether this is the last page

- **`PaginationRequestDTO`**: Request parameters for pagination:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20, max: 100)
  - `sortBy`: Field to sort by
  - `sortDirection`: Sort direction (ASC/DESC)

### 2. Updated Services

The following services now support pagination on their main GET endpoints:

#### Individual Microservices (with Spring Data JPA pagination):
- **Candidate Service**: `/candidates` (with pagination parameters)
- **Vendor Service**: `/vendors` (with pagination parameters)
- **Position Service**: `/positions` (with pagination parameters)
- **Job Requisition Service**: `/job-requisitions` (with pagination parameters)
- **Organization Service**: `/organizations` (with pagination parameters)
- **Tenant Service**: `/api/v1/tenants` (with pagination parameters)
- **Application Service**: `/applications` (with pagination parameters)

#### HCM Core Service (aggregator):
- **Candidates**: `/candidates` (with pagination parameters)
- **Vendors**: `/vendors` (with pagination parameters)
- **Positions**: `/positions` (with pagination parameters)
- **Job Requisitions**: `/job-requisitions` (with pagination parameters)
- **Organizations**: `/organizations` (with pagination parameters)
- **Tenants**: `/tenants` (with pagination parameters)
- **Applications**: `/applications` (with pagination parameters)

### 3. API Endpoints

All GET endpoints that return lists now accept the following query parameters:

- `page` (optional): Page number, defaults to 0
- `size` (optional): Page size, defaults to 20, maximum 100
- `sortBy` (optional): Field to sort by, defaults to entity ID
- `sortDirection` (optional): ASC or DESC, defaults to ASC

### 4. Response Format

All paginated endpoints return a `PaginatedResponseDTO` with the following structure:

```json
{
  "content": [
    // Array of entity DTOs
  ],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 150,
  "totalPages": 8,
  "hasNext": true,
  "hasPrevious": false,
  "isFirst": true,
  "isLast": false
}
```

## Usage Examples

### Get first page of candidates (20 items)
```
GET /candidates
```

### Get second page of vendors with custom page size
```
GET /vendors?page=1&size=10
```

### Get positions sorted by title in descending order
```
GET /positions?sortBy=title&sortDirection=DESC
```

### Get job requisitions with custom pagination
```
GET /job-requisitions?page=2&size=15&sortBy=createdAt&sortDirection=DESC
```

### Get applications with pagination
```
GET /applications?page=0&size=25&sortBy=applicationId&sortDirection=DESC
```

## Implementation Details

### Individual Microservices
- Use Spring Data JPA's `Pageable` and `Page` interfaces
- Implement pagination at the repository level for optimal performance
- Return Spring Data `Page` objects directly
- Main GET endpoints now support pagination parameters instead of separate `/paginated` endpoints

### HCM Core Service
- Acts as an aggregator that calls individual microservices
- Currently implements client-side pagination as a workaround for WebClient deserialization
- In production, should be updated to properly deserialize Spring Data `Page` objects
- Main GET endpoints now support pagination parameters instead of separate `/paginated` endpoints

## Fixed Issues

### Application Controller
- Added missing `getAllApplications()` endpoint to hcm-core ApplicationController
- Added `getAllApplicationsPaginated()` endpoint with pagination support
- Updated application-service to support pagination at the repository level

### Candidate Endpoints
- Verified that candidate-service has proper `/candidates` endpoint
- Confirmed that hcm-core candidate endpoints are correctly calling the candidate-service

### Pagination Approach
- **CORRECTED**: Removed separate `/paginated` endpoints
- **CORRECTED**: Applied pagination parameters to all main GET endpoints that return lists
- **CORRECTED**: Made pagination consistent across all services

## Future Improvements

1. **Proper WebClient Deserialization**: Implement proper deserialization of Spring Data `Page` objects in the hcm-core service
2. **Server-Side Pagination**: Update hcm-core to use the actual paginated endpoints from microservices
3. **Caching**: Add caching for frequently accessed paginated data
4. **Filtering**: Add support for filtering in addition to pagination
5. **Search**: Implement full-text search with pagination

## Notes

- The current implementation in hcm-core uses client-side pagination as a temporary solution
- All individual microservices support proper server-side pagination using Spring Data JPA
- Page numbers are 0-based (first page is page 0)
- Maximum page size is limited to 100 to prevent performance issues
- Default page size is 20 items per page
- All major entities now have consistent pagination support
- **Pagination is applied to ALL GET endpoints that return lists, not separate endpoints** 