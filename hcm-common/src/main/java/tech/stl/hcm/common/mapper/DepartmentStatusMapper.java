package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.DepartmentStatus;
import tech.stl.hcm.common.dto.DepartmentStatusDTO;
import tech.stl.hcm.common.dto.helpers.DepartmentStatusCreateDTO;

import java.time.Instant;

public class DepartmentStatusMapper {

    public static DepartmentStatusDTO toDTO(DepartmentStatus entity) {
        if (entity == null) return null;
        return DepartmentStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static DepartmentStatus toEntity(DepartmentStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return DepartmentStatus.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 