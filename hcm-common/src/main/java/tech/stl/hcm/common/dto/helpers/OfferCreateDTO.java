package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferCreateDTO {
    private Integer applicationId;
    private UUID candidateId;
    private Integer requisitionId;
    private Integer statusId;
    private BigDecimal salary;
    private String currency;
    private LocalDate offerDate;
    private LocalDate acceptanceDeadline;
    private UUID createdBy;
} 