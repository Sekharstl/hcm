package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdTypeDTO {
    private Integer idTypeId;
    private UUID tenantId;
    private String typeName;
    private String description;
    private Boolean isRequired;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
} 