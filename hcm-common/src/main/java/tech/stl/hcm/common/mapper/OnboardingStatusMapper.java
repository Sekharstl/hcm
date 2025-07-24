package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.OnboardingStatus;
import tech.stl.hcm.common.dto.OnboardingStatusDTO;
import tech.stl.hcm.common.dto.helpers.OnboardingStatusCreateDTO;

import java.time.Instant;

public class OnboardingStatusMapper {

    public static OnboardingStatusDTO toDTO(OnboardingStatus entity) {
        if (entity == null) return null;
        return OnboardingStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static OnboardingStatus toEntity(OnboardingStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return OnboardingStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 