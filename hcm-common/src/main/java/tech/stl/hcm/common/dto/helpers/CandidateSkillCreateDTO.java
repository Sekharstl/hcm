package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateSkillCreateDTO {
    private UUID candidateId;
    private Integer skillId;
    private String proficiencyLevel;
    private Integer yearsOfExperience;
    private UUID createdBy;
} 