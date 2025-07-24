package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCertificationCreateDTO {
    private UUID candidateId;
    private String certificateName;
    private String issuedBy;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private UUID createdBy;
    private String certificationName;
    private String issuingOrganization;
} 