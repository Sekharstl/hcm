package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.UserRole;

public class UserRoleSpecification {

    public static Specification<UserRole> hasName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

    // Add more specifications as needed
} 