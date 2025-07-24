package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.User;
import tech.stl.hcm.common.dto.UserDTO;
import tech.stl.hcm.common.dto.helpers.UserCreateDTO;

import java.time.Instant;
import java.util.UUID;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        if (entity == null) return null;
        return UserDTO.builder()
                .userId(entity.getUserId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .roleId(entity.getRoleId())
                .typeId(entity.getTypeId())
                .statusId(entity.getStatusId())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static User toEntity(UserCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return User.builder()
                .userId(UUID.randomUUID())
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .roleId(dto.getRoleId())
                .typeId(dto.getTypeId())
                .statusId(dto.getStatusId())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 