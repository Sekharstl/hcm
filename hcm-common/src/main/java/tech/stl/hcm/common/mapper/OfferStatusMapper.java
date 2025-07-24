package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.OfferStatus;
import tech.stl.hcm.common.dto.OfferStatusDTO;
import tech.stl.hcm.common.dto.helpers.OfferStatusCreateDTO;

import java.time.Instant;

public class OfferStatusMapper {

    public static OfferStatusDTO toDTO(OfferStatus entity) {
        if (entity == null) return null;
        return OfferStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static OfferStatus toEntity(OfferStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return OfferStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 