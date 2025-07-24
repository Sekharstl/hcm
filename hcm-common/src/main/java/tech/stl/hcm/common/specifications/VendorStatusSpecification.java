package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.VendorStatus;

public class VendorStatusSpecification {

    public static Specification<VendorStatus> hasName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

    // Add more specifications as needed
} 