package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.PositionStatus;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionStatusCreateDTO;

import java.time.Instant;

public class PositionStatusMapper {

    public static PositionStatusDTO toDTO(PositionStatus entity) {
        if (entity == null) return null;
        return PositionStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static PositionStatus toEntity(PositionStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return PositionStatus.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static PositionStatusCreateDTO toCreateDTO(PositionStatusDTO dto) {
        if (dto == null) return null;
        return PositionStatusCreateDTO.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 