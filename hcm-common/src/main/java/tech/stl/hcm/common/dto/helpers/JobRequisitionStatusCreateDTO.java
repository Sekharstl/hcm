package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequisitionStatusCreateDTO {
    private String name;
    private UUID createdBy;
} 