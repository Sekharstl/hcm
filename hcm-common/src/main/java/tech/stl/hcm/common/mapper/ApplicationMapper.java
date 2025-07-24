package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Application;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import java.time.Instant;
import java.util.UUID;

public class ApplicationMapper {
    public static ApplicationDTO toDTO(Application entity) {
        if (entity == null) return null;
        return ApplicationDTO.builder()
                .applicationId(entity.getApplicationId())
                .candidateId(entity.getCandidateId())
                .requisitionId(entity.getRequisitionId())
                .statusId(entity.getStatusId())
                .appliedDate(entity.getAppliedDate())
                .source(entity.getSource())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
    public static Application toEntity(ApplicationCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Application.builder()
                .candidateId(dto.getCandidateId())
                .requisitionId(dto.getRequisitionId())
                .statusId(dto.getStatusId())
                .appliedDate(dto.getAppliedDate())
                .source(dto.getSource())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
    public static void updateEntity(Application entity, ApplicationDTO dto) {
        entity.setStatusId(dto.getStatusId());
        entity.setAppliedDate(dto.getAppliedDate());
        entity.setSource(dto.getSource());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }
    public static ApplicationCreateDTO toCreateDTO(ApplicationDTO dto) {
        if (dto == null) return null;
        return ApplicationCreateDTO.builder()
                .candidateId(dto.getCandidateId())
                .requisitionId(dto.getRequisitionId())
                .statusId(dto.getStatusId())
                .appliedDate(dto.getAppliedDate())
                .source(dto.getSource())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 