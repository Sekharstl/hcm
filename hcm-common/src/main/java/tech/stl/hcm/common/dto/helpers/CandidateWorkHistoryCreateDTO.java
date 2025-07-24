package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateWorkHistoryCreateDTO {
    private UUID candidateId;
    private String companyName;
    private String positionTitle;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String responsibilities;
    private UUID createdBy;
    private String description;
    private String jobTitle;
} 