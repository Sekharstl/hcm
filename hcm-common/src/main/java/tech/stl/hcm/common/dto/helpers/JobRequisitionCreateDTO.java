package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequisitionCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private Integer positionId;
    private Integer departmentId;
    private String title;
    private String location;
    private String employmentType;
    private LocalDate postedDate;
    private LocalDate closingDate;
    private Integer statusId;
    private UUID hiringManagerId;
    private UUID vendorId;
    private UUID createdBy;
} 