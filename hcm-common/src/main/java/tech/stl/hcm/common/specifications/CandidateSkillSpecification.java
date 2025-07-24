package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.CandidateSkill;

import java.util.UUID;

public class CandidateSkillSpecification {

    public static Specification<CandidateSkill> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<CandidateSkill> hasSkillId(Integer skillId) {
        return (root, query, cb) -> cb.equal(root.get("skillId"), skillId);
    }

    // Add more specifications as needed
} 