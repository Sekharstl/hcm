package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingCreateDTO {
    private Integer offerId;
    private UUID candidateId;
    private Integer statusId;
    private LocalDate startDate;
    private LocalDate orientationDate;
    private UUID createdBy;
} 