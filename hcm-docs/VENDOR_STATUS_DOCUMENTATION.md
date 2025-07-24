# Vendor Status Documentation

## Overview
The vendor status system in the HCM application manages the lifecycle of vendors (external service providers, recruitment agencies, etc.) through various status states.

## Vendor Status Table Structure
```sql
CREATE TABLE hcm.vendor_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);
```

## Available Vendor Statuses

### 1. Active (ID: 1)
- **Description**: Vendor is currently operational and providing services
- **Use Case**: Vendors who are actively supplying candidates or services
- **Business Rules**: 
  - Can submit candidates
  - Can be assigned to job requisitions
  - Eligible for payments and commissions

### 2. Inactive (ID: 2)
- **Description**: Vendor is temporarily not providing services
- **Use Case**: Vendors on temporary hiatus or seasonal breaks
- **Business Rules**:
  - Cannot submit new candidates
  - Existing candidates remain in the system
  - Can be reactivated to Active status

### 3. Pending Approval (ID: 3)
- **Description**: New vendor awaiting approval from administrators
- **Use Case**: New vendor registrations that require review
- **Business Rules**:
  - Cannot submit candidates until approved
  - Requires admin approval to move to Active status
  - May require additional documentation

### 4. Suspended (ID: 4)
- **Description**: Vendor temporarily suspended due to compliance or performance issues
- **Use Case**: Vendors with quality issues or compliance violations
- **Business Rules**:
  - Cannot submit new candidates
  - Existing candidates may be reassigned
  - Requires review before reactivation

### 5. Blacklisted (ID: 5)
- **Description**: Vendor permanently barred from the system
- **Use Case**: Vendors with serious violations or fraud
- **Business Rules**:
  - Cannot submit candidates
  - Cannot be reactivated
  - All existing candidates should be reviewed

### 6. Under Review (ID: 6)
- **Description**: Vendor being evaluated for performance or compliance
- **Use Case**: Vendors with quality concerns or audit requirements
- **Business Rules**:
  - Limited candidate submission
  - Enhanced monitoring
  - Requires review outcome

### 7. Contract Expired (ID: 7)
- **Description**: Vendor's contract has expired and needs renewal
- **Use Case**: Vendors with expired service agreements
- **Business Rules**:
  - Cannot submit new candidates
  - Existing candidates remain active
  - Requires contract renewal

### 8. On Hold (ID: 8)
- **Description**: Vendor temporarily paused due to administrative reasons
- **Use Case**: Vendors awaiting documentation or administrative processes
- **Business Rules**:
  - Cannot submit new candidates
  - Temporary status
  - Can be reactivated quickly

## Status Transition Rules

### Allowed Transitions:
- **Pending Approval** → **Active** (after approval)
- **Pending Approval** → **Inactive** (if rejected)
- **Active** → **Inactive** (voluntary or administrative)
- **Active** → **Suspended** (due to issues)
- **Active** → **Under Review** (for evaluation)
- **Active** → **Contract Expired** (when contract ends)
- **Inactive** → **Active** (reactivation)
- **Suspended** → **Active** (after review)
- **Suspended** → **Blacklisted** (serious violations)
- **Under Review** → **Active** (positive review)
- **Under Review** → **Suspended** (negative review)
- **Contract Expired** → **Active** (after renewal)
- **On Hold** → **Active** (after resolution)

### Restricted Transitions:
- **Blacklisted** → Any other status (permanent ban)
- **Active** → **Blacklisted** (requires serious violation)

## Implementation Notes

### Database Constraints
- The `vendor_status` table has a unique constraint on the `name` field
- Foreign key relationship exists between `vendor.status_id` and `vendor_status.status_id`
- Triggers automatically update `updated_at` and `updated_by` fields

### Application Integration
- Status changes should be logged for audit purposes
- Email notifications should be sent to vendors on status changes
- Status changes may trigger workflow processes

### API Considerations
- Status changes should require appropriate permissions
- Bulk status updates should be handled carefully
- Status history should be maintained for audit trails

## Usage Examples

### SQL Queries

```sql
-- Get all active vendors
SELECT * FROM hcm.vendor WHERE status_id = 1;

-- Get vendors by status
SELECT v.*, vs.name as status_name 
FROM hcm.vendor v 
JOIN hcm.vendor_status vs ON v.status_id = vs.status_id;

-- Count vendors by status
SELECT vs.name, COUNT(v.vendor_id) as vendor_count
FROM hcm.vendor_status vs
LEFT JOIN hcm.vendor v ON vs.status_id = v.status_id
GROUP BY vs.status_id, vs.name;
```

### Java Entity Usage

```java
// Get vendor status by name
VendorStatus activeStatus = vendorStatusRepository.findByName("Active");

// Update vendor status
vendor.setStatusId(activeStatus.getStatusId());
vendorRepository.save(vendor);
```

## Migration and Deployment

The vendor status data is provided in two formats:
1. `vendor_status_data.sql` - Standalone SQL file for manual execution
2. `V1__create_vendor_status_data.sql` - Flyway migration for automated deployment

To deploy:
1. Run the migration file in your vendor service
2. Verify the data is inserted correctly
3. Test status transitions in your application

## Monitoring and Maintenance

### Regular Tasks:
- Monitor status distribution
- Review vendors in "Under Review" status
- Clean up expired contracts
- Audit blacklisted vendors

### Performance Considerations:
- Index on `vendor.status_id` for efficient queries
- Consider caching status data for frequently accessed statuses
- Monitor status change frequency for optimization opportunities 