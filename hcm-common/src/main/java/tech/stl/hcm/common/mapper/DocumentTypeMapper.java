package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.DocumentType;
import tech.stl.hcm.common.dto.DocumentTypeDTO;

@Component
public class DocumentTypeMapper {

    public DocumentTypeDTO toDTO(DocumentType entity) {
        if (entity == null) {
            return null;
        }

        return DocumentTypeDTO.builder()
                .documentTypeId(entity.getDocumentTypeId())
                .tenantId(entity.getTenantId())
                .typeName(entity.getTypeName())
                .description(entity.getDescription())
                .isRequired(entity.getIsRequired())
                .maxFileSize(entity.getMaxFileSize())
                .allowedExtensions(entity.getAllowedExtensions())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public DocumentType toEntity(DocumentTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        return DocumentType.builder()
                .documentTypeId(dto.getDocumentTypeId())
                .tenantId(dto.getTenantId())
                .typeName(dto.getTypeName())
                .description(dto.getDescription())
                .isRequired(dto.getIsRequired())
                .maxFileSize(dto.getMaxFileSize())
                .allowedExtensions(dto.getAllowedExtensions())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
} 