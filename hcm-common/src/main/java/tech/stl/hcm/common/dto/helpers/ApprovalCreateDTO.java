package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalCreateDTO {
    private Integer requisitionId;
    private UUID approverId;
    private Integer statusId;
    private LocalDate actionDate;
    private String comments;
    private UUID createdBy;
} 