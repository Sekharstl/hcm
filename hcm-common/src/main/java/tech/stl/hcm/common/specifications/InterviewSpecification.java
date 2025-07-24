package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Interview;

import java.util.UUID;

public class InterviewSpecification {

    public static Specification<Interview> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<Interview> hasRequisitionId(Integer requisitionId) {
        return (root, query, cb) -> cb.equal(root.get("requisitionId"), requisitionId);
    }

    // Add more specifications as needed
} 