package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.UserStatus;
import tech.stl.hcm.common.dto.UserStatusDTO;
import tech.stl.hcm.common.dto.helpers.UserStatusCreateDTO;

import java.time.Instant;

public class UserStatusMapper {

    public static UserStatusDTO toDTO(UserStatus entity) {
        if (entity == null) return null;
        return UserStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static UserStatus toEntity(UserStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return UserStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 