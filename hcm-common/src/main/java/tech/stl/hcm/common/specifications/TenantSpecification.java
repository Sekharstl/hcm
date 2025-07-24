package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Tenant;

public class TenantSpecification {

    public static Specification<Tenant> hasDomain(String domain) {
        return (root, query, cb) -> cb.equal(root.get("domain"), domain);
    }

    public static Specification<Tenant> hasName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

    // Add more specifications as needed
} 