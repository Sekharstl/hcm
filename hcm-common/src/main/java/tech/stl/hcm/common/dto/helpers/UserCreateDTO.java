package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Integer roleId;
    private Integer typeId;
    private Integer statusId;
    private UUID createdBy;
} 