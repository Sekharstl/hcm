package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.Approval;

public class ApprovalSpecification {

    public static Specification<Approval> hasRequisitionId(Integer requisitionId) {
        return (root, query, cb) -> cb.equal(root.get("requisitionId"), requisitionId);
    }

    public static Specification<Approval> hasApproverId(java.util.UUID approverId) {
        return (root, query, cb) -> cb.equal(root.get("approverId"), approverId);
    }

    // Add more specifications as needed
} 