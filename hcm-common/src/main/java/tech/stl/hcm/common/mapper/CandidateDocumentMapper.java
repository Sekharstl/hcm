package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.CandidateDocument;
import tech.stl.hcm.common.dto.CandidateDocumentDTO;

@Component
public class CandidateDocumentMapper {

    public CandidateDocumentDTO toDTO(CandidateDocument entity) {
        if (entity == null) {
            return null;
        }

        return CandidateDocumentDTO.builder()
                .documentId(entity.getDocumentId())
                .candidateId(entity.getCandidateId())
                .documentTypeId(entity.getDocumentTypeId())
                .fileName(entity.getFileName())
                .originalFileName(entity.getOriginalFileName())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .mimeType(entity.getMimeType())
                .uploadDate(entity.getUploadDate())
                .expiryDate(entity.getExpiryDate())
                .isVerified(entity.getIsVerified())
                .verificationDate(entity.getVerificationDate())
                .verifiedBy(entity.getVerifiedBy())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .documentTypeName(entity.getDocumentType() != null ? entity.getDocumentType().getTypeName() : null)
                .downloadUrl("/api/v1/candidates/documents/" + entity.getDocumentId() + "/download")
                .build();
    }

    public CandidateDocument toEntity(CandidateDocumentDTO dto) {
        if (dto == null) {
            return null;
        }

        return CandidateDocument.builder()
                .documentId(dto.getDocumentId())
                .candidateId(dto.getCandidateId())
                .documentTypeId(dto.getDocumentTypeId())
                .fileName(dto.getFileName())
                .originalFileName(dto.getOriginalFileName())
                .filePath(dto.getFilePath())
                .fileSize(dto.getFileSize())
                .mimeType(dto.getMimeType())
                .uploadDate(dto.getUploadDate())
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