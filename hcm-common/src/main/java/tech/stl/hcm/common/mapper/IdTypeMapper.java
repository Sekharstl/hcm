package tech.stl.hcm.common.mapper;

import org.springframework.stereotype.Component;
import tech.stl.hcm.common.db.entities.IdType;
import tech.stl.hcm.common.dto.IdTypeDTO;

@Component
public class IdTypeMapper {

    public IdTypeDTO toDTO(IdType entity) {
        if (entity == null) {
            return null;
        }

        return IdTypeDTO.builder()
                .idTypeId(entity.getIdTypeId())
                .tenantId(entity.getTenantId())
                .typeName(entity.getTypeName())
                .description(entity.getDescription())
                .isRequired(entity.getIsRequired())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public IdType toEntity(IdTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        return IdType.builder()
                .idTypeId(dto.getIdTypeId())
                .tenantId(dto.getTenantId())
                .typeName(dto.getTypeName())
                .description(dto.getDescription())
                .isRequired(dto.getIsRequired())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
} 