package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeDTO {
    private Integer documentTypeId;
    private UUID tenantId;
    private String typeName;
    private String description;
    private Boolean isRequired;
    private Integer maxFileSize;
    private String allowedExtensions;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
} 