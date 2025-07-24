package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.CandidateIdentity;
import tech.stl.hcm.common.dto.CandidateIdentityDTO;

@Component
public class CandidateIdentityMapper {

    public CandidateIdentityDTO toDTO(CandidateIdentity entity) {
        if (entity == null) {
            return null;
        }

        return CandidateIdentityDTO.builder()
                .identityId(entity.getIdentityId())
                .candidateId(entity.getCandidateId())
                .idTypeId(entity.getIdTypeId())
                .idNumber(entity.getIdNumber())
                .issuingCountry(entity.getIssuingCountry())
                .issueDate(entity.getIssueDate())
                .expiryDate(entity.getExpiryDate())
                .isVerified(entity.getIsVerified())
                .verificationDate(entity.getVerificationDate())
                .verifiedBy(entity.getVerifiedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .idTypeName(entity.getIdType() != null ? entity.getIdType().getTypeName() : null)
                .build();
    }

    public CandidateIdentity toEntity(CandidateIdentityDTO dto) {
        if (dto == null) {
            return null;
        }

        return CandidateIdentity.builder()
                .identityId(dto.getIdentityId())
                .candidateId(dto.getCandidateId())
                .idTypeId(dto.getIdTypeId())
                .idNumber(dto.getIdNumber())
                .issuingCountry(dto.getIssuingCountry())
                .issueDate(dto.getIssueDate())
                .expiryDate(dto.getExpiryDate())
                .isVerified(dto.getIsVerified())
                .verificationDate(dto.getVerificationDate())
                .verifiedBy(dto.getVerifiedBy())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
} 