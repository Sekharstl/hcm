package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.CandidateEducation;

import java.time.LocalDate;
import java.util.UUID;

public class CandidateEducationSpecification {

    public static Specification<CandidateEducation> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<CandidateEducation> hasInstitution(String institution) {
        return (root, query, cb) -> cb.like(root.get("institution"), "%" + institution + "%");
    }

    public static Specification<CandidateEducation> hasDegree(String degree) {
        return (root, query, cb) -> cb.like(root.get("degree"), "%" + degree + "%");
    }

    public static Specification<CandidateEducation> hasFieldOfStudy(String fieldOfStudy) {
        return (root, query, cb) -> cb.like(root.get("fieldOfStudy"), "%" + fieldOfStudy + "%");
    }

    public static Specification<CandidateEducation> hasStartDate(LocalDate startDate) {
        return (root, query, cb) -> cb.equal(root.get("startDate"), startDate);
    }

    public static Specification<CandidateEducation> hasEndDate(LocalDate endDate) {
        return (root, query, cb) -> cb.equal(root.get("endDate"), endDate);
    }

    public static Specification<CandidateEducation> hasGrade(String grade) {
        return (root, query, cb) -> cb.like(root.get("grade"), "%" + grade + "%");
    }

    public static Specification<CandidateEducation> hasNotes(String notes) {
        return (root, query, cb) -> cb.like(root.get("notes"), "%" + notes + "%");
    }

    public static Specification<CandidateEducation> hasDescription(String description) {
        return (root, query, cb) -> cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<CandidateEducation> hasInstitutionName(String institutionName) {
        return (root, query, cb) -> cb.like(root.get("institutionName"), "%" + institutionName + "%");
    }

    public static Specification<CandidateEducation> isActive() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<CandidateEducation> isDeleted() {
        return (root, query, cb) -> cb.isNotNull(root.get("deletedAt"));
    }

    public static Specification<CandidateEducation> hasCreatedBy(UUID createdBy) {
        return (root, query, cb) -> cb.equal(root.get("createdBy"), createdBy);
    }

    public static Specification<CandidateEducation> hasUpdatedBy(UUID updatedBy) {
        return (root, query, cb) -> cb.equal(root.get("updatedBy"), updatedBy);
    }
} 