package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.OrganizationStatus;
import tech.stl.hcm.common.dto.OrganizationStatusDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationStatusCreateDTO;

import java.time.Instant;

public class OrganizationStatusMapper {

    public static OrganizationStatusDTO toDTO(OrganizationStatus entity) {
        if (entity == null) return null;
        return OrganizationStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static OrganizationStatus toEntity(OrganizationStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return OrganizationStatus.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 