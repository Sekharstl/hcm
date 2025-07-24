package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.CandidateSkill;
import tech.stl.hcm.common.dto.CandidateSkillDTO;
import tech.stl.hcm.common.dto.helpers.CandidateSkillCreateDTO;

import java.time.Instant;

public class CandidateSkillMapper {

    public static CandidateSkillDTO toDTO(CandidateSkill entity) {
        if (entity == null) return null;
        return CandidateSkillDTO.builder()
                .candidateId(entity.getCandidateId())
                .skillId(entity.getSkillId())
                .proficiencyLevel(entity.getProficiencyLevel())
                .yearsOfExperience(entity.getYearsOfExperience())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static CandidateSkill toEntity(CandidateSkillCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return CandidateSkill.builder()
                .candidateId(dto.getCandidateId())
                .skillId(dto.getSkillId())
                .proficiencyLevel(dto.getProficiencyLevel())
                .yearsOfExperience(dto.getYearsOfExperience())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static void updateEntity(CandidateSkill entity, CandidateSkillDTO dto) {
        entity.setProficiencyLevel(dto.getProficiencyLevel());
        entity.setYearsOfExperience(dto.getYearsOfExperience());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }

    public static CandidateSkillCreateDTO toCreateDTO(CandidateSkillDTO dto) {
        if (dto == null) return null;
        return CandidateSkillCreateDTO.builder()
                .candidateId(dto.getCandidateId())
                .skillId(dto.getSkillId())
                .proficiencyLevel(dto.getProficiencyLevel())
                .yearsOfExperience(dto.getYearsOfExperience())
                .createdBy(dto.getCreatedBy())
                .build();
    }
} 