package tech.stl.hcm.common.dto.helpers;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateIdentityCreateDTO {
    private UUID candidateId;
    private Integer idTypeId;
    private String idNumber;
    private String issuingCountry;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private UUID createdBy;
} 