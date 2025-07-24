package tech.stl.hcm.common.dto.helpers;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCreateDTO {
    private UUID tenantId;
    private UUID organizationId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String middleName;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String linkedinUrl;
    private java.math.BigDecimal currentSalary;
    private java.math.BigDecimal expectedSalary;
    private Integer noticePeriod;
    private java.time.LocalDate availabilityDate;
    private String status;
    private String source;
    private UUID vendorId;
    private String notes;
    private UUID createdBy;
} 