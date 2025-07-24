package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateIdentityDTO {
    private Integer identityId;
    private UUID candidateId;
    private Integer idTypeId;
    private String idNumber;
    private String issuingCountry;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Boolean isVerified;
    private Instant verificationDate;
    private UUID verifiedBy;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
    
    // Additional fields for UI
    private String idTypeName;
} 