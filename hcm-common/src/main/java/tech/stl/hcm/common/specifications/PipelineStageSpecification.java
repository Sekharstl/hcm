package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.PipelineStage;

public class PipelineStageSpecification {

    public static Specification<PipelineStage> hasRequisitionId(Integer requisitionId) {
        return (root, query, cb) -> cb.equal(root.get("requisitionId"), requisitionId);
    }

    // Add more specifications as needed
} 