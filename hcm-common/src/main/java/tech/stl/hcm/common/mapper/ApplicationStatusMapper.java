package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.ApplicationStatus;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import java.time.Instant;

public class ApplicationStatusMapper {
    public static ApplicationStatusDTO toDTO(ApplicationStatus entity) {
        if (entity == null) return null;
        return ApplicationStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
    public static ApplicationStatus toEntity(ApplicationStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return ApplicationStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
    public static void updateEntity(ApplicationStatus entity, ApplicationStatusDTO dto) {
        entity.setName(dto.getName());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }
    public static ApplicationStatusCreateDTO toCreateDTO(ApplicationStatusDTO dto) {
        if (dto == null) return null;
        return ApplicationStatusCreateDTO.builder()
                .name(dto.getName())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 