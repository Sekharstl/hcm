package tech.stl.hcm.common.dto.helpers;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateReferenceCreateDTO {
    private UUID candidateId;
    private String referenceName;
    private String relationship;
    private String company;
    private String position;
    private String email;
    private String phone;
    private UUID createdBy;
} 