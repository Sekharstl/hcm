package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.ApprovalStatus;
import tech.stl.hcm.common.dto.ApprovalStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApprovalStatusCreateDTO;

import java.time.Instant;

public class ApprovalStatusMapper {

    public static ApprovalStatusDTO toDTO(ApprovalStatus entity) {
        if (entity == null) return null;
        return ApprovalStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static ApprovalStatus toEntity(ApprovalStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return ApprovalStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 