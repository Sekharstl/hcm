package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Offer;

import java.util.UUID;

public class OfferSpecification {

    public static Specification<Offer> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<Offer> hasRequisitionId(Integer requisitionId) {
        return (root, query, cb) -> cb.equal(root.get("requisitionId"), requisitionId);
    }

    // Add more specifications as needed
} 