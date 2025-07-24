package tech.stl.hcm.common.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Integer countryId;
    private String countryCode;
    private String countryName;
    private String phoneCode;
    private String currencyCode;
    private Boolean isActive;
    private Instant createdAt;
    private UUID createdBy;
    private Instant updatedAt;
    private UUID updatedBy;
} 