package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Onboarding;
import tech.stl.hcm.common.dto.OnboardingDTO;
import tech.stl.hcm.common.dto.helpers.OnboardingCreateDTO;

import java.time.Instant;

public class OnboardingMapper {

    public static OnboardingDTO toDTO(Onboarding entity) {
        if (entity == null) return null;
        return OnboardingDTO.builder()
                .onboardingId(entity.getOnboardingId())
                .offerId(entity.getOfferId())
                .candidateId(entity.getCandidateId())
                .statusId(entity.getStatusId())
                .startDate(entity.getStartDate())
                .orientationDate(entity.getOrientationDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Onboarding toEntity(OnboardingCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Onboarding.builder()
                .offerId(dto.getOfferId())
                .candidateId(dto.getCandidateId())
                .statusId(dto.getStatusId())
                .startDate(dto.getStartDate())
                .orientationDate(dto.getOrientationDate())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 