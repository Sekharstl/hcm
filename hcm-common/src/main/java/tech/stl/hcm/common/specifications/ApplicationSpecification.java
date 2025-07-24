package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Application;

import java.util.UUID;

public class ApplicationSpecification {

    public static Specification<Application> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<Application> hasRequisitionId(Integer requisitionId) {
        return (root, query, cb) -> cb.equal(root.get("requisitionId"), requisitionId);
    }

    // Add more specifications as needed
} 