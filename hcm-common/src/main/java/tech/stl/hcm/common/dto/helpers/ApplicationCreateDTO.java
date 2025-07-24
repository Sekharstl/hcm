package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreateDTO {
    private UUID candidateId;
    private Integer requisitionId;
    private Integer statusId;
    private LocalDate appliedDate;
    private String source;
    private UUID createdBy;
} 