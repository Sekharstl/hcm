package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Department;
import tech.stl.hcm.common.dto.DepartmentDTO;
import tech.stl.hcm.common.dto.helpers.DepartmentCreateDTO;

import java.time.Instant;

public class DepartmentMapper {

    public static DepartmentDTO toDTO(Department entity) {
        if (entity == null) return null;
        return DepartmentDTO.builder()
                .departmentId(entity.getDepartmentId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .name(entity.getName())
                .parentDepartmentId(entity.getParentDepartmentId())
                .statusId(entity.getStatusId())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Department toEntity(DepartmentCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Department.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .name(dto.getName())
                .parentDepartmentId(dto.getParentDepartmentId())
                .statusId(dto.getStatusId())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static DepartmentCreateDTO toCreateDTO(DepartmentDTO dto) {
        if (dto == null) return null;
        return DepartmentCreateDTO.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .name(dto.getName())
                .parentDepartmentId(dto.getParentDepartmentId())
                .statusId(dto.getStatusId())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 