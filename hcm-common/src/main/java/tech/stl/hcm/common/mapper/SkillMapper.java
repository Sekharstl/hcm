package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Skill;
import tech.stl.hcm.common.dto.SkillDTO;
import tech.stl.hcm.common.dto.helpers.SkillCreateDTO;

import java.time.Instant;

public class SkillMapper {

    public static SkillDTO toDTO(Skill entity) {
        if (entity == null) return null;
        return SkillDTO.builder()
                .skillId(entity.getSkillId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .skillName(entity.getSkillName())
                .skillCategory(entity.getSkillCategory())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Skill toEntity(SkillCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Skill.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .skillName(dto.getSkillName())
                .skillCategory(dto.getSkillCategory())
                .description(dto.getDescription())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }
} 