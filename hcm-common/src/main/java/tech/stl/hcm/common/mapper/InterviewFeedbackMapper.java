package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.InterviewFeedback;
import tech.stl.hcm.common.dto.InterviewFeedbackDTO;
import tech.stl.hcm.common.dto.helpers.InterviewFeedbackCreateDTO;

import java.time.Instant;

public class InterviewFeedbackMapper {

    public static InterviewFeedbackDTO toDTO(InterviewFeedback entity) {
        if (entity == null) return null;
        return InterviewFeedbackDTO.builder()
                .feedbackId(entity.getFeedbackId())
                .interviewId(entity.getInterviewId())
                .interviewerId(entity.getInterviewerId())
                .candidateId(entity.getCandidateId())
                .feedback(entity.getFeedback())
                .rating(entity.getRating())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static InterviewFeedback toEntity(InterviewFeedbackCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return InterviewFeedback.builder()
                .interviewId(dto.getInterviewId())
                .interviewerId(dto.getInterviewerId())
                .candidateId(dto.getCandidateId())
                .feedback(dto.getFeedback())
                .rating(dto.getRating())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 