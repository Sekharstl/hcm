package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreateDTO {
    private UUID tenantId;
    private String name;
    private String address;
    private Integer statusId;
    private UUID createdBy;
} 