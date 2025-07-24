package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.JobRequisitionStatus;
import tech.stl.hcm.common.dto.JobRequisitionStatusDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionStatusCreateDTO;

import java.time.Instant;

public class JobRequisitionStatusMapper {

    public static JobRequisitionStatusDTO toDTO(JobRequisitionStatus entity) {
        if (entity == null) return null;
        return JobRequisitionStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static JobRequisitionStatus toEntity(JobRequisitionStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return JobRequisitionStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static JobRequisitionStatusCreateDTO toCreateDTO(JobRequisitionStatusDTO dto) {
        // Map fields from dto to createDTO as needed
        JobRequisitionStatusCreateDTO createDTO = new JobRequisitionStatusCreateDTO();
        createDTO.setName(dto.getName());
        createDTO.setCreatedBy(dto.getCreatedBy());                // ... map other fields ...
        return createDTO;
    }
} 