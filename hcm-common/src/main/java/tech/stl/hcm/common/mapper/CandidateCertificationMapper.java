package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.CandidateCertification;
import tech.stl.hcm.common.dto.CandidateCertificationDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCertificationCreateDTO;

import java.time.Instant;

public class CandidateCertificationMapper {

    public static CandidateCertificationDTO toDTO(CandidateCertification entity) {
        if (entity == null) return null;
        return CandidateCertificationDTO.builder()
                .certificationId(entity.getCertificationId())
                .candidateId(entity.getCandidateId())
                .certificateName(entity.getCertificateName())
                .issuedBy(entity.getIssuedBy())
                .issueDate(entity.getIssueDate())
                .expiryDate(entity.getExpiryDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .certificationName(entity.getCertificationName())
                .issuingOrganization(entity.getIssuingOrganization())
                .build();
    }

    public static CandidateCertification toEntity(CandidateCertificationCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return CandidateCertification.builder()
                .candidateId(dto.getCandidateId())
                .certificateName(dto.getCertificateName())
                .issuedBy(dto.getIssuedBy())
                .issueDate(dto.getIssueDate())
                .expiryDate(dto.getExpiryDate())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .certificationName(dto.getCertificationName())
                .issuingOrganization(dto.getIssuingOrganization())
                .build();
    }

    public static void updateEntity(CandidateCertification entity, CandidateCertificationDTO dto) {
        entity.setCertificateName(dto.getCertificateName());
        entity.setIssuedBy(dto.getIssuedBy());
        entity.setIssueDate(dto.getIssueDate());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setCertificationName(dto.getCertificationName());
        entity.setIssuingOrganization(dto.getIssuingOrganization());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }

    public static CandidateCertificationCreateDTO toCreateDTO(CandidateCertificationDTO dto) {
        if (dto == null) return null;
        return CandidateCertificationCreateDTO.builder()
                .candidateId(dto.getCandidateId())
                .certificateName(dto.getCertificateName())
                .issuedBy(dto.getIssuedBy())
                .issueDate(dto.getIssueDate())
                .expiryDate(dto.getExpiryDate())
                .createdBy(dto.getCreatedBy())
                .certificationName(dto.getCertificationName())
                .issuingOrganization(dto.getIssuingOrganization())
                .build();
    }
} 