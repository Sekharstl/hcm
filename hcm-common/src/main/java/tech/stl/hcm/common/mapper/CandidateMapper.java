package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Candidate;
import tech.stl.hcm.common.dto.CandidateDTO;
import tech.stl.hcm.common.dto.helpers.CandidateCreateDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class CandidateMapper {

    public static CandidateDTO toDTO(Candidate entity) {
        if (entity == null) return null;
        return CandidateDTO.builder()
                .candidateId(entity.getCandidateId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .nationality(entity.getNationality())
                .middleName(entity.getMiddleName())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .linkedinUrl(entity.getLinkedinUrl())
                .currentSalary(entity.getCurrentSalary())
                .expectedSalary(entity.getExpectedSalary())
                .noticePeriod(entity.getNoticePeriod())
                .availabilityDate(entity.getAvailabilityDate())
                .status(entity.getStatus())
                .source(entity.getSource())
                .vendorId(entity.getVendorId())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .deletedBy(entity.getDeletedBy())
                .build();
    }

    public static Candidate toEntity(CandidateCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Candidate.builder()
                .candidateId(UUID.randomUUID())
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .nationality(dto.getNationality())
                .middleName(dto.getMiddleName())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .linkedinUrl(dto.getLinkedinUrl())
                .currentSalary(dto.getCurrentSalary())
                .expectedSalary(dto.getExpectedSalary())
                .noticePeriod(dto.getNoticePeriod())
                .availabilityDate(dto.getAvailabilityDate())
                .status(dto.getStatus())
                .source(dto.getSource())
                .vendorId(dto.getVendorId())
                .notes(dto.getNotes())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static void updateEntity(Candidate entity, CandidateDTO dto) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setNationality(dto.getNationality());
        entity.setMiddleName(dto.getMiddleName());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
        entity.setPostalCode(dto.getPostalCode());
        entity.setLinkedinUrl(dto.getLinkedinUrl());
        entity.setCurrentSalary(dto.getCurrentSalary());
        entity.setExpectedSalary(dto.getExpectedSalary());
        entity.setNoticePeriod(dto.getNoticePeriod());
        entity.setAvailabilityDate(dto.getAvailabilityDate());
        entity.setStatus(dto.getStatus());
        entity.setSource(dto.getSource());
        entity.setVendorId(dto.getVendorId());
        entity.setNotes(dto.getNotes());
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }

    public static CandidateCreateDTO toCreateDTO(CandidateDTO dto) {
        if (dto == null) return null;
        return CandidateCreateDTO.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .nationality(dto.getNationality())
                .middleName(dto.getMiddleName())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .linkedinUrl(dto.getLinkedinUrl())
                .currentSalary(dto.getCurrentSalary())
                .expectedSalary(dto.getExpectedSalary())
                .noticePeriod(dto.getNoticePeriod())
                .availabilityDate(dto.getAvailabilityDate())
                .status(dto.getStatus())
                .source(dto.getSource())
                .vendorId(dto.getVendorId())
                .notes(dto.getNotes())
                .createdBy(dto.getCreatedBy())
                .build();
    }

    public static CandidateDTO fromMap(Map<String, Object> map) {
        if (map == null) return null;
        
        return CandidateDTO.builder()
                .candidateId(map.get("candidateId") != null ? UUID.fromString(map.get("candidateId").toString()) : null)
                .tenantId(map.get("tenantId") != null ? UUID.fromString(map.get("tenantId").toString()) : null)
                .organizationId(map.get("organizationId") != null ? UUID.fromString(map.get("organizationId").toString()) : null)
                .firstName((String) map.get("firstName"))
                .lastName((String) map.get("lastName"))
                .email((String) map.get("email"))
                .phone((String) map.get("phone"))
                .address((String) map.get("address"))
                .dateOfBirth(map.get("dateOfBirth") != null ? LocalDate.parse(map.get("dateOfBirth").toString()) : null)
                .gender((String) map.get("gender"))
                .nationality((String) map.get("nationality"))
                .middleName((String) map.get("middleName"))
                .city((String) map.get("city"))
                .state((String) map.get("state"))
                .country((String) map.get("country"))
                .postalCode((String) map.get("postalCode"))
                .linkedinUrl((String) map.get("linkedinUrl"))
                .currentSalary(map.get("currentSalary") != null ? new BigDecimal(map.get("currentSalary").toString()) : null)
                .expectedSalary(map.get("expectedSalary") != null ? new BigDecimal(map.get("expectedSalary").toString()) : null)
                .noticePeriod(map.get("noticePeriod") != null ? Integer.valueOf(map.get("noticePeriod").toString()) : null)
                .availabilityDate(map.get("availabilityDate") != null ? LocalDate.parse(map.get("availabilityDate").toString()) : null)
                .status((String) map.get("status"))
                .source((String) map.get("source"))
                .vendorId(map.get("vendorId") != null ? UUID.fromString(map.get("vendorId").toString()) : null)
                .notes((String) map.get("notes"))
                .createdAt(map.get("createdAt") != null ? Instant.parse(map.get("createdAt").toString()) : null)
                .createdBy(map.get("createdBy") != null ? UUID.fromString(map.get("createdBy").toString()) : null)
                .updatedAt(map.get("updatedAt") != null ? Instant.parse(map.get("updatedAt").toString()) : null)
                .updatedBy(map.get("updatedBy") != null ? UUID.fromString(map.get("updatedBy").toString()) : null)
                .deletedAt(map.get("deletedAt") != null ? Instant.parse(map.get("deletedAt").toString()) : null)
                .deletedBy(map.get("deletedBy") != null ? UUID.fromString(map.get("deletedBy").toString()) : null)
                .build();
    }
} 