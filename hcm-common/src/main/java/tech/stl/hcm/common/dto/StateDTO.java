package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateDTO {
    private Integer stateId;
    private Integer countryId;
    private String stateCode;
    private String stateName;
    private Boolean isActive;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
    
    // Additional fields for UI
    private String countryName;
    private String countryCode;
} 