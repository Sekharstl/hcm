package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Onboarding;

import java.util.UUID;

public class OnboardingSpecification {

    public static Specification<Onboarding> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<Onboarding> hasOfferId(Integer offerId) {
        return (root, query, cb) -> cb.equal(root.get("offerId"), offerId);
    }

    // Add more specifications as needed
} 