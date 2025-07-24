package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Interview;
import tech.stl.hcm.common.dto.InterviewDTO;
import tech.stl.hcm.common.dto.helpers.InterviewCreateDTO;

import java.time.Instant;

public class InterviewMapper {

    public static InterviewDTO toDTO(Interview entity) {
        if (entity == null) return null;
        return InterviewDTO.builder()
                .interviewId(entity.getInterviewId())
                .applicationId(entity.getApplicationId())
                .candidateId(entity.getCandidateId())
                .requisitionId(entity.getRequisitionId())
                .interviewerId(entity.getInterviewerId())
                .statusId(entity.getStatusId())
                .scheduledDate(entity.getScheduledDate())
                .mode(entity.getMode())
                .location(entity.getLocation())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Interview toEntity(InterviewCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Interview.builder()
                .applicationId(dto.getApplicationId())
                .candidateId(dto.getCandidateId())
                .requisitionId(dto.getRequisitionId())
                .interviewerId(dto.getInterviewerId())
                .statusId(dto.getStatusId())
                .scheduledDate(dto.getScheduledDate())
                .mode(dto.getMode())
                .location(dto.getLocation())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 