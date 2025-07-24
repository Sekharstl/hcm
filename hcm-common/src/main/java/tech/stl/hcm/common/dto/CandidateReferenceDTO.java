package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateReferenceDTO {
    private Integer referenceId;
    private UUID candidateId;
    private String referenceName;
    private String relationship;
    private String company;
    private String position;
    private String email;
    private String phone;
    private Boolean isVerified;
    private Instant verificationDate;
    private UUID verifiedBy;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
} 