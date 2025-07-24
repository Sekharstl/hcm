package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private String name;
    private Integer parentDepartmentId;
    private Integer statusId;
    private UUID createdBy;
} 