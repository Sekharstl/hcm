package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewCreateDTO {
    private Integer applicationId;
    private UUID candidateId;
    private Integer requisitionId;
    private UUID interviewerId;
    private Integer statusId;
    private LocalDate scheduledDate;
    private String mode;
    private String location;
    private UUID createdBy;
} 