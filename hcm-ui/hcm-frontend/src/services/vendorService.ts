export async function fetchVendorById(id: string) {
  // TODO: Replace with actual API call
  // Mock data for demonstration
  return {
    vendorId: id,
    tenantId: 'tenant-uuid',
    organizationId: 'org-uuid',
    vendorName: 'Sample Vendor',
    contactName: 'John Doe',
    contactEmail: 'john@sample.com',
    contactPhone: '+1 234-567-8901',
    address: '123 Vendor Street',
    statusId: 1,
    createdAt: new Date().toISOString(),
    createdBy: 'user-uuid',
    updatedAt: new Date().toISOString(),
    updatedBy: 'user-uuid',
  };
} 