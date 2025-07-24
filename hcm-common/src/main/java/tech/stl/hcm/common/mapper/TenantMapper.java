package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Tenant;
import tech.stl.hcm.common.dto.TenantDTO;
import tech.stl.hcm.common.dto.helpers.TenantCreateDTO;

import java.time.Instant;
import java.util.UUID;

public class TenantMapper {

    public static TenantDTO toDTO(Tenant entity) {
        if (entity == null) return null;
        return TenantDTO.builder()
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .domain(entity.getDomain())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Tenant toEntity(TenantCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Tenant.builder()
                .tenantId(UUID.randomUUID())
                .name(dto.getName())
                .domain(dto.getDomain())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static void updateEntity(Tenant entity, TenantDTO dto) {
        entity.setName(dto.getName());
        entity.setDomain(dto.getDomain());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }

    public static TenantCreateDTO toCreateDTO(TenantDTO dto) {
        if (dto == null) return null;
        return TenantCreateDTO.builder()
                .name(dto.getName())
                .domain(dto.getDomain())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 