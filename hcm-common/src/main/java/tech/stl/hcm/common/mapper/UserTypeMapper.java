package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.UserType;
import tech.stl.hcm.common.dto.UserTypeDTO;
import tech.stl.hcm.common.dto.helpers.UserTypeCreateDTO;

import java.time.Instant;

public class UserTypeMapper {

    public static UserTypeDTO toDTO(UserType entity) {
        if (entity == null) return null;
        return UserTypeDTO.builder()
                .typeId(entity.getTypeId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static UserType toEntity(UserTypeCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return UserType.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 