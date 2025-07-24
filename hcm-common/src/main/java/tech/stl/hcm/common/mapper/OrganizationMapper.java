package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Organization;
import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;

import java.time.Instant;
import java.util.UUID;

public class OrganizationMapper {

    public static OrganizationDTO toDTO(Organization entity) {
        if (entity == null) return null;
        return OrganizationDTO.builder()
                .organizationId(entity.getOrganizationId())
                .tenantId(entity.getTenantId())
                .name(entity.getName())
                .address(entity.getAddress())
                .statusId(entity.getStatusId())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Organization toEntity(OrganizationCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Organization.builder()
                .organizationId(UUID.randomUUID())
                .tenantId(dto.getTenantId())
                .name(dto.getName())
                .address(dto.getAddress())
                .statusId(dto.getStatusId())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static OrganizationCreateDTO toCreateDTO(OrganizationDTO dto) {
        if (dto == null) return null;
        return OrganizationCreateDTO.builder()
                .tenantId(dto.getTenantId())
                .name(dto.getName())
                .address(dto.getAddress())
                .statusId(dto.getStatusId())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 