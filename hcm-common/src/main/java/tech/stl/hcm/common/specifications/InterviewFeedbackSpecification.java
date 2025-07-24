package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.InterviewFeedback;

import java.util.UUID;

public class InterviewFeedbackSpecification {

    public static Specification<InterviewFeedback> hasInterviewId(Integer interviewId) {
        return (root, query, cb) -> cb.equal(root.get("interviewId"), interviewId);
    }

    public static Specification<InterviewFeedback> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    // Add more specifications as needed
} 