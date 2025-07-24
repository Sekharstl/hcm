package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Position;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;

import java.time.Instant;

public class PositionMapper {

    public static PositionDTO toDTO(Position entity) {
        if (entity == null) return null;
        return PositionDTO.builder()
                .positionId(entity.getPositionId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .departmentId(entity.getDepartmentId())
                .title(entity.getTitle())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .employmentType(entity.getEmploymentType())
                .statusId(entity.getStatusId())
                .headcount(entity.getHeadcount())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Position toEntity(PositionCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Position.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .departmentId(dto.getDepartmentId())
                .title(dto.getTitle())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .employmentType(dto.getEmploymentType())
                .statusId(dto.getStatusId())
                .headcount(dto.getHeadcount() != null ? dto.getHeadcount() : 1)
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static PositionCreateDTO toCreateDTO(PositionDTO dto) {
        if (dto == null) return null;
        return PositionCreateDTO.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .departmentId(dto.getDepartmentId())
                .title(dto.getTitle())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .employmentType(dto.getEmploymentType())
                .statusId(dto.getStatusId())
                .headcount(dto.getHeadcount())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 