package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.UserRole;
import tech.stl.hcm.common.dto.UserRoleDTO;
import tech.stl.hcm.common.dto.helpers.UserRoleCreateDTO;

import java.time.Instant;

public class UserRoleMapper {

    public static UserRoleDTO toDTO(UserRole entity) {
        if (entity == null) return null;
        return UserRoleDTO.builder()
                .roleId(entity.getRoleId())
                .name(entity.getName())
                .permissions(entity.getPermissions())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static UserRole toEntity(UserRoleCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return UserRole.builder()
                .name(dto.getName())
                .permissions(dto.getPermissions())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 