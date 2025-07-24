package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.VendorStatus;
import tech.stl.hcm.common.dto.VendorStatusDTO;
import tech.stl.hcm.common.dto.helpers.VendorStatusCreateDTO;

import java.time.Instant;

public class VendorStatusMapper {

    public static VendorStatusDTO toDTO(VendorStatus entity) {
        if (entity == null) return null;
        return VendorStatusDTO.builder()
                .statusId(entity.getStatusId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static VendorStatus toEntity(VendorStatusCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return VendorStatus.builder()
                .name(dto.getName())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 