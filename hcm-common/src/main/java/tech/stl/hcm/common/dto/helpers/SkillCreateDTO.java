package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private String skillName;
    private String skillCategory;
    private String description;
    private UUID createdBy;
} 