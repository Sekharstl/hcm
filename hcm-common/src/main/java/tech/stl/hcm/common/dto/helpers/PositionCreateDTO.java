package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private Integer departmentId;
    private String title;
    private String location;
    private String description;
    private String employmentType;
    private Integer statusId;
    private Integer headcount;
    private UUID createdBy;
} 