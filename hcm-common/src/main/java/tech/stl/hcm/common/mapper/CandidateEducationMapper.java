package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.CandidateEducation;
import tech.stl.hcm.common.dto.CandidateEducationDTO;
import tech.stl.hcm.common.dto.helpers.CandidateEducationCreateDTO;

import java.time.Instant;

public class CandidateEducationMapper {

    public static CandidateEducationDTO toDTO(CandidateEducation entity) {
        if (entity == null) return null;
        return CandidateEducationDTO.builder()
                .educationId(entity.getEducationId())
                .candidateId(entity.getCandidateId())
                .institution(entity.getInstitution())
                .degree(entity.getDegree())
                .fieldOfStudy(entity.getFieldOfStudy())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .grade(entity.getGrade())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .description(entity.getDescription())
                .institutionName(entity.getInstitutionName())
                .build();
    }

    public static CandidateEducation toEntity(CandidateEducationCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return CandidateEducation.builder()
                .candidateId(dto.getCandidateId())
                .institution(dto.getInstitution())
                .degree(dto.getDegree())
                .fieldOfStudy(dto.getFieldOfStudy())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .grade(dto.getGrade())
                .notes(dto.getNotes())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .description(dto.getDescription())
                .institutionName(dto.getInstitutionName())
                .build();
    }

    public static void updateEntity(CandidateEducation entity, CandidateEducationDTO dto) {
        entity.setInstitution(dto.getInstitution());
        entity.setDegree(dto.getDegree());
        entity.setFieldOfStudy(dto.getFieldOfStudy());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setGrade(dto.getGrade());
        entity.setNotes(dto.getNotes());
        entity.setDescription(dto.getDescription());
        entity.setInstitutionName(dto.getInstitutionName());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }

    public static CandidateEducationCreateDTO toCreateDTO(CandidateEducationDTO dto) {
        if (dto == null) return null;
        return CandidateEducationCreateDTO.builder()
                .candidateId(dto.getCandidateId())
                .institution(dto.getInstitution())
                .degree(dto.getDegree())
                .fieldOfStudy(dto.getFieldOfStudy())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .grade(dto.getGrade())
                .notes(dto.getNotes())
                .createdBy(dto.getCreatedBy())
                .description(dto.getDescription())
                .institutionName(dto.getInstitutionName())
                .build();
    }
} 