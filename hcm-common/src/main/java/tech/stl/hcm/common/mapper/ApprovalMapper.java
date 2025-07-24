package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Approval;
import tech.stl.hcm.common.dto.ApprovalDTO;
import tech.stl.hcm.common.dto.helpers.ApprovalCreateDTO;

import java.time.Instant;
import java.time.LocalDate;

public class ApprovalMapper {

    public static ApprovalDTO toDTO(Approval entity) {
        if (entity == null) return null;
        return ApprovalDTO.builder()
                .approvalId(entity.getApprovalId())
                .requisitionId(entity.getRequisitionId())
                .approverId(entity.getApproverId())
                .statusId(entity.getStatusId())
                .actionDate(entity.getActionDate())
                .comments(entity.getComments())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Approval toEntity(ApprovalCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        LocalDate actionDate = dto.getActionDate() != null ? dto.getActionDate() : LocalDate.now();
        return Approval.builder()
                .requisitionId(dto.getRequisitionId())
                .approverId(dto.getApproverId())
                .statusId(dto.getStatusId())
                .actionDate(actionDate)
                .comments(dto.getComments())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 