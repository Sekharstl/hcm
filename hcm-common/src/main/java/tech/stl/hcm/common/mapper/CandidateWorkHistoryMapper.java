package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.CandidateWorkHistory;
import tech.stl.hcm.common.dto.CandidateWorkHistoryDTO;
import tech.stl.hcm.common.dto.helpers.CandidateWorkHistoryCreateDTO;

import java.time.Instant;

public class CandidateWorkHistoryMapper {

    public static CandidateWorkHistoryDTO toDTO(CandidateWorkHistory entity) {
        if (entity == null) return null;
        return CandidateWorkHistoryDTO.builder()
                .workHistoryId(entity.getWorkHistoryId())
                .candidateId(entity.getCandidateId())
                .companyName(entity.getCompanyName())
                .positionTitle(entity.getPositionTitle())
                .location(entity.getLocation())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .responsibilities(entity.getResponsibilities())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .description(entity.getDescription())
                .jobTitle(entity.getJobTitle())
                .build();
    }

    public static CandidateWorkHistory toEntity(CandidateWorkHistoryCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return CandidateWorkHistory.builder()
                .candidateId(dto.getCandidateId())
                .companyName(dto.getCompanyName())
                .positionTitle(dto.getPositionTitle())
                .location(dto.getLocation())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .responsibilities(dto.getResponsibilities())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .description(dto.getDescription())
                .jobTitle(dto.getJobTitle())
                .build();
    }

    public static void updateEntity(CandidateWorkHistory entity, CandidateWorkHistoryDTO dto) {
        // Example: update fields from dto to entity
        entity.setCompanyName(dto.getCompanyName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setResponsibilities(dto.getResponsibilities());
        entity.setDescription(dto.getDescription());
        entity.setJobTitle(dto.getJobTitle());
        entity.setLocation(dto.getLocation());
        entity.setPositionTitle(dto.getPositionTitle());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
        // ... update other fields as needed
    }

    public static CandidateWorkHistoryCreateDTO toCreateDTO(CandidateWorkHistoryDTO dto) {
        if (dto == null) return null;
        return CandidateWorkHistoryCreateDTO.builder()
                .candidateId(dto.getCandidateId())
                .companyName(dto.getCompanyName())
                .positionTitle(dto.getPositionTitle())
                .location(dto.getLocation())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .responsibilities(dto.getResponsibilities())
                .createdBy(dto.getCreatedBy())
                .description(dto.getDescription())
                .jobTitle(dto.getJobTitle())
                .build();
    }
} 