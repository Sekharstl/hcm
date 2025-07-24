package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private String vendorName;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private Integer statusId;
    private UUID createdBy;
} 