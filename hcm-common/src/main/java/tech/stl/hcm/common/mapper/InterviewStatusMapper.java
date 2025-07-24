package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.InterviewStatus;
import tech.stl.hcm.common.dto.InterviewStatusDTO;
import tech.stl.hcm.common.dto.helpers.InterviewStatusCreateDTO;

import java.time.Instant;

public class InterviewStatusMapper {

    public static InterviewStatusDTO toDTO(InterviewStatus entity) {
        if (entity == null) return null;
        return InterviewStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static InterviewStatus toEntity(InterviewStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return InterviewStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 