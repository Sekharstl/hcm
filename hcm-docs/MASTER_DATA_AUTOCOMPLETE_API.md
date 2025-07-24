# Master Data Autocomplete API Documentation

This document describes the autocompletion endpoints available for master data entities in the HCM system. These endpoints provide search functionality with partial text matching for improved user experience in forms and dropdowns.

## Architecture

The master data autocompletion system follows a layered architecture:

- **Controller Layer** (`MasterDataController`): Handles HTTP requests and responses
- **Service Layer** (`MasterDataService` & `MasterDataServiceImpl`): Contains business logic and data processing
- **Repository Layer**: Data access layer for database operations
- **Mapper Layer**: Converts between entities and DTOs

## Base URL
All endpoints are prefixed with `/master-data`

## Available Endpoints

### 1. Document Types

#### Get All Document Types
```
GET /master-data/document-types
```
Returns all document types in the system.

#### Get Document Type by ID
```
GET /master-data/document-types/{id}
```
Returns a specific document type by its ID.

#### Search Document Types (Autocomplete)
```
GET /master-data/document-types/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against document type names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/document-types/search?query=passport&limit=5
```

### 2. ID Types

#### Get All ID Types
```
GET /master-data/id-types
```
Returns all ID types in the system.

#### Get ID Type by ID
```
GET /master-data/id-types/{id}
```
Returns a specific ID type by its ID.

#### Search ID Types (Autocomplete)
```
GET /master-data/id-types/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against ID type names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/id-types/search?query=driver&limit=5
```

### 3. Countries

#### Get All Countries
```
GET /master-data/countries
```
Returns all countries in the system.

#### Get Country by ID
```
GET /master-data/countries/{id}
```
Returns a specific country by its ID.

#### Get Country by Code
```
GET /master-data/countries/code/{code}
```
Returns a specific country by its country code (e.g., "US", "CA").

#### Search Countries (Autocomplete)
```
GET /master-data/countries/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against country names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/countries/search?query=united&limit=5
```

### 4. States

#### Get All States
```
GET /master-data/states
```
Returns all states in the system.

#### Get State by ID
```
GET /master-data/states/{id}
```
Returns a specific state by its ID.

#### Get States by Country ID
```
GET /master-data/states/country/{countryId}
```
Returns all states for a specific country.

#### Get States by Country Code
```
GET /master-data/states/country-code/{countryCode}
```
Returns all states for a specific country code.

#### Search States (Autocomplete)
```
GET /master-data/states/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against state names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/states/search?query=california&limit=5
```

#### Search States by Country (Autocomplete)
```
GET /master-data/states/country/{countryId}/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `countryId` (required): Country ID to filter states
- `query` (required): Search term to match against state names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/states/country/1/search?query=california&limit=5
```

#### Search States by Country Code (Autocomplete)
```
GET /master-data/states/country-code/{countryCode}/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `countryCode` (required): Country code to filter states (e.g., "US")
- `query` (required): Search term to match against state names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/states/country-code/US/search?query=california&limit=5
```

### 5. Skills

#### Get All Skills
```
GET /master-data/skills
```
Returns all skills in the system.

#### Get Skill by ID
```
GET /master-data/skills/{id}
```
Returns a specific skill by its ID.

#### Search Skills (Autocomplete)
```
GET /master-data/skills/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against skill names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/skills/search?query=java&limit=5
```

#### Search Skills by Category (Autocomplete)
```
GET /master-data/skills/category/{category}/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `category` (required): Skill category to filter by
- `query` (required): Search term to match against skill names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/skills/category/programming/search?query=java&limit=5
```

### 6. Departments

#### Get All Departments
```
GET /master-data/departments
```
Returns all departments in the system.

#### Get Department by ID
```
GET /master-data/departments/{id}
```
Returns a specific department by its ID.

#### Search Departments (Autocomplete)
```
GET /master-data/departments/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against department names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/departments/search?query=engineering&limit=5
```

### 7. User Roles

#### Get All User Roles
```
GET /master-data/user-roles
```
Returns all user roles in the system.

#### Get User Role by ID
```
GET /master-data/user-roles/{id}
```
Returns a specific user role by its ID.

#### Search User Roles (Autocomplete)
```
GET /master-data/user-roles/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against role names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/user-roles/search?query=admin&limit=5
```

### 8. User Types

#### Get All User Types
```
GET /master-data/user-types
```
Returns all user types in the system.

#### Get User Type by ID
```
GET /master-data/user-types/{id}
```
Returns a specific user type by its ID.

#### Search User Types (Autocomplete)
```
GET /master-data/user-types/search?query={searchTerm}&limit={limit}
```
**Parameters:**
- `query` (required): Search term to match against user type names
- `limit` (optional): Maximum number of results (default: 10)

**Example:**
```
GET /master-data/user-types/search?query=employee&limit=5
```

## Response Format

All endpoints return JSON responses with the following structure:

### Success Response
```json
[
  {
    "id": 1,
    "name": "Example Name",
    // ... other fields specific to the entity
  }
]
```

### Error Response
```json
{
  "error": "Error message",
  "status": 500
}
```

## Usage Examples

### Frontend Integration

#### JavaScript Example
```javascript
// Search for countries
async function searchCountries(query, limit = 10) {
  try {
    const response = await fetch(`/master-data/countries/search?query=${encodeURIComponent(query)}&limit=${limit}`);
    const countries = await response.json();
    return countries;
  } catch (error) {
    console.error('Error searching countries:', error);
    return [];
  }
}

// Search for states within a country
async function searchStatesByCountry(countryCode, query, limit = 10) {
  try {
    const response = await fetch(`/master-data/states/country-code/${countryCode}/search?query=${encodeURIComponent(query)}&limit=${limit}`);
    const states = await response.json();
    return states;
  } catch (error) {
    console.error('Error searching states:', error);
    return [];
  }
}
```

#### React Component Example
```jsx
import React, { useState, useEffect } from 'react';

function AutocompleteInput({ endpoint, placeholder, onSelect }) {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const searchData = async () => {
      if (query.length < 2) {
        setResults([]);
        return;
      }

      setLoading(true);
      try {
        const response = await fetch(`${endpoint}?query=${encodeURIComponent(query)}&limit=10`);
        const data = await response.json();
        setResults(data);
      } catch (error) {
        console.error('Search error:', error);
        setResults([]);
      } finally {
        setLoading(false);
      }
    };

    const timeoutId = setTimeout(searchData, 300);
    return () => clearTimeout(timeoutId);
  }, [query, endpoint]);

  return (
    <div className="autocomplete-container">
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder={placeholder}
        className="autocomplete-input"
      />
      {loading && <div className="loading">Loading...</div>}
      {results.length > 0 && (
        <ul className="autocomplete-results">
          {results.map((item) => (
            <li
              key={item.id}
              onClick={() => {
                onSelect(item);
                setQuery('');
                setResults([]);
              }}
            >
              {item.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

// Usage
<AutocompleteInput
  endpoint="/master-data/countries/search"
  placeholder="Search countries..."
  onSelect={(country) => console.log('Selected:', country)}
/>
```

## Performance Considerations

1. **Debouncing**: Implement debouncing on the frontend to avoid excessive API calls while typing
2. **Minimum Query Length**: Consider requiring a minimum query length (e.g., 2 characters) before making API calls
3. **Caching**: Consider implementing client-side caching for frequently searched terms
4. **Pagination**: Use the `limit` parameter to control the number of results returned

## Error Handling

All endpoints include comprehensive error handling:
- Invalid parameters return appropriate HTTP status codes
- Database errors are logged and return 500 status codes
- Missing resources return 404 status codes

## Security

- All endpoints are protected by authentication and authorization
- Input validation is performed on all query parameters
- SQL injection protection is implemented through parameterized queries 