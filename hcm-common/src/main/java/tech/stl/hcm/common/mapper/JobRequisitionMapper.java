package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.JobRequisition;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;

import java.time.Instant;
import java.time.LocalDate;

public class JobRequisitionMapper {

    public static JobRequisitionDTO toDTO(JobRequisition entity) {
        if (entity == null) return null;
        return JobRequisitionDTO.builder()
                .jobRequisitionId(entity.getRequisitionId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .positionId(entity.getPositionId())
                .title(entity.getTitle())
                .statusId(entity.getStatusId())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static JobRequisition toEntity(JobRequisitionCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        LocalDate postedDate = dto.getPostedDate() != null ? dto.getPostedDate() : LocalDate.now();
        return JobRequisition.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .positionId(dto.getPositionId())
                .departmentId(dto.getDepartmentId())
                .title(dto.getTitle())
                .employmentType(dto.getEmploymentType())
                .postedDate(postedDate)
                .closingDate(dto.getClosingDate())
                .statusId(dto.getStatusId())
                .hiringManagerId(dto.getHiringManagerId())
                .vendorId(dto.getVendorId())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static JobRequisitionCreateDTO toCreateDTO(JobRequisitionDTO dto) {
        if (dto == null) return null;
        return JobRequisitionCreateDTO.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .positionId(dto.getPositionId())
                .title(dto.getTitle())
                // .employmentType(dto.getEmploymentType())
                .statusId(dto.getStatusId())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 