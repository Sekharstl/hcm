package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Organization;

import java.util.UUID;

public class OrganizationSpecification {

    public static Specification<Organization> hasTenantId(UUID tenantId) {
        return (root, query, cb) -> cb.equal(root.get("tenantId"), tenantId);
    }

    public static Specification<Organization> hasStatusId(Integer statusId) {
        return (root, query, cb) -> cb.equal(root.get("statusId"), statusId);
    }

    // Add more specifications as needed
} 