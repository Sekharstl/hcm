package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.CandidateWorkHistory;

import java.util.UUID;

public class CandidateWorkHistorySpecification {

    public static Specification<CandidateWorkHistory> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    // Add more specifications as needed
} 