package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Vendor;

import java.util.UUID;

public class VendorSpecification {

    public static Specification<Vendor> hasTenantId(UUID tenantId) {
        return (root, query, cb) -> cb.equal(root.get("tenantId"), tenantId);
    }

    public static Specification<Vendor> hasOrganizationId(UUID organizationId) {
        return (root, query, cb) -> cb.equal(root.get("organizationId"), organizationId);
    }

    // Add more specifications as needed
} 