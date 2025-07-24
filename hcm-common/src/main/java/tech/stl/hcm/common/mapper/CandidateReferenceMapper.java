package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.CandidateReference;
import tech.stl.hcm.common.dto.CandidateReferenceDTO;

@Component
public class CandidateReferenceMapper {

    public CandidateReferenceDTO toDTO(CandidateReference entity) {
        if (entity == null) {
            return null;
        }

        return CandidateReferenceDTO.builder()
                .referenceId(entity.getReferenceId())
                .candidateId(entity.getCandidateId())
                .referenceName(entity.getReferenceName())
                .relationship(entity.getRelationship())
                .company(entity.getCompany())
                .position(entity.getPosition())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .isVerified(entity.getIsVerified())
                .verificationDate(entity.getVerificationDate())
                .verifiedBy(entity.getVerifiedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public CandidateReference toEntity(CandidateReferenceDTO dto) {
        if (dto == null) {
            return null;
        }

        return CandidateReference.builder()
                .referenceId(dto.getReferenceId())
                .candidateId(dto.getCandidateId())
                .referenceName(dto.getReferenceName())
                .relationship(dto.getRelationship())
                .company(dto.getCompany())
                .position(dto.getPosition())
                .email(dto.getEmail())
                .phone(dto.getPhone())
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