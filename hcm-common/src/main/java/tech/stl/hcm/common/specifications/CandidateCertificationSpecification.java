package tech.stl.hcm.common.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.stl.hcm.common.db.entities.CandidateCertification;

import java.time.LocalDate;
import java.util.UUID;

public class CandidateCertificationSpecification {

    public static Specification<CandidateCertification> hasCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<CandidateCertification> byCandidateId(UUID candidateId) {
        return (root, query, cb) -> cb.equal(root.get("candidateId"), candidateId);
    }

    public static Specification<CandidateCertification> byCertificationId(Integer certificationId) {
        return (root, query, cb) -> cb.equal(root.get("certificationId"), certificationId);
    }

    public static Specification<CandidateCertification> byCertificationName(String certificationName) {
        return (root, query, cb) -> cb.equal(root.get("certificationName"), certificationName);
    }

    public static Specification<CandidateCertification> byCertificationProvider(String certificationProvider) {
        return (root, query, cb) -> cb.equal(root.get("certificationProvider"), certificationProvider);
    }

    public static Specification<CandidateCertification> byCertificationDate(LocalDate certificationDate) {
        return (root, query, cb) -> cb.equal(root.get("certificationDate"), certificationDate);
    }

    public static Specification<CandidateCertification> byCertificationStatus(String certificationStatus) {
        return (root, query, cb) -> cb.equal(root.get("certificationStatus"), certificationStatus);
    }

    public static Specification<CandidateCertification> byCertificationExpirationDate(LocalDate certificationExpirationDate) {
        return (root, query, cb) -> cb.equal(root.get("certificationExpirationDate"), certificationExpirationDate);
    }

    public static Specification<CandidateCertification> byCertificationVersion(String certificationVersion) {
        return (root, query, cb) -> cb.equal(root.get("certificationVersion"), certificationVersion);
    }

    public static Specification<CandidateCertification> byCertificationUrl(String certificationUrl) {
        return (root, query, cb) -> cb.equal(root.get("certificationUrl"), certificationUrl);
    }

    public static Specification<CandidateCertification> byCertificationScore(Double certificationScore) {
        return (root, query, cb) -> cb.equal(root.get("certificationScore"), certificationScore);
    }

    public static Specification<CandidateCertification> byCertificationPassingScore(Double certificationPassingScore) {
        return (root, query, cb) -> cb.equal(root.get("certificationPassingScore"), certificationPassingScore);
    }

    public static Specification<CandidateCertification> byCertificationMaxScore(Double certificationMaxScore) {
        return (root, query, cb) -> cb.equal(root.get("certificationMaxScore"), certificationMaxScore);
    }

    public static Specification<CandidateCertification> byCertificationMinScore(Double certificationMinScore) {
        return (root, query, cb) -> cb.equal(root.get("certificationMinScore"), certificationMinScore);
    }

    public static Specification<CandidateCertification> byCertificationWeight(Double certificationWeight) {
        return (root, query, cb) -> cb.equal(root.get("certificationWeight"), certificationWeight);
    }

    public static Specification<CandidateCertification> byCertificationDescription(String certificationDescription) {
        return (root, query, cb) -> cb.equal(root.get("certificationDescription"), certificationDescription);
    }

    public static Specification<CandidateCertification> byCertificationType(String certificationType) {
        return (root, query, cb) -> cb.equal(root.get("certificationType"), certificationType);
    }

}
