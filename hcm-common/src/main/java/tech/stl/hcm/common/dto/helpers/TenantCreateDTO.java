package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantCreateDTO {
    private String name;
    private String domain;
    private UUID createdBy;
} 