package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.PipelineStage;
import tech.stl.hcm.common.dto.PipelineStageDTO;
import tech.stl.hcm.common.dto.helpers.PipelineStageCreateDTO;

import java.time.Instant;

public class PipelineStageMapper {

    public static PipelineStageDTO toDTO(PipelineStage entity) {
        if (entity == null) return null;
        return PipelineStageDTO.builder()
                .stageId(entity.getStageId())
                .requisitionId(entity.getRequisitionId())
                .name(entity.getName())
                .sequence(entity.getSequence())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static PipelineStage toEntity(PipelineStageCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return PipelineStage.builder()
                .requisitionId(dto.getRequisitionId())
                .name(dto.getName())
                .sequence(dto.getSequence())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 