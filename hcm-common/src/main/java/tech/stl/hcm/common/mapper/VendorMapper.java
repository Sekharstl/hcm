package tech.stl.hcm.common.mapper;

import tech.stl.hcm.common.db.entities.Vendor;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class VendorMapper {

    public static VendorDTO toDTO(Vendor entity) {
        if (entity == null) return null;
        return VendorDTO.builder()
                .vendorId(entity.getVendorId())
                .tenantId(entity.getTenantId())
                .organizationId(entity.getOrganizationId())
                .vendorName(entity.getVendorName())
                .contactName(entity.getContactName())
                .contactEmail(entity.getContactEmail())
                .contactPhone(entity.getContactPhone())
                .address(entity.getAddress())
                .statusId(entity.getStatusId())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public static Vendor toEntity(VendorCreateDTO dto) {
        if (dto == null) return null;
        Instant now = Instant.now();
        return Vendor.builder()
                .vendorId(UUID.randomUUID())
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .vendorName(dto.getVendorName())
                .contactName(dto.getContactName())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .address(dto.getAddress())
                .statusId(dto.getStatusId())
                .createdAt(now)
                .createdBy(dto.getCreatedBy())
                .updatedAt(now)
                .updatedBy(dto.getCreatedBy())
                .build();
    }

    public static VendorCreateDTO toCreateDTO(VendorDTO dto) {
        if (dto == null) return null;
        return VendorCreateDTO.builder()
                .tenantId(dto.getTenantId())
                .organizationId(dto.getOrganizationId())
                .vendorName(dto.getVendorName())
                .contactName(dto.getContactName())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .address(dto.getAddress())
                .statusId(dto.getStatusId())
                .createdBy(dto.getCreatedBy())
                .build();
    }

    public static void updateEntity(Vendor entity, VendorDTO dto) {
        if (entity == null || dto == null) return;
        entity.setTenantId(dto.getTenantId());
        entity.setOrganizationId(dto.getOrganizationId());
        entity.setVendorName(dto.getVendorName());
        entity.setContactName(dto.getContactName());
        entity.setContactEmail(dto.getContactEmail());
        entity.setContactPhone(dto.getContactPhone());
        entity.setAddress(dto.getAddress());
        entity.setStatusId(dto.getStatusId());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setUpdatedBy(dto.getUpdatedBy());
    }

    public static VendorDTO fromMap(Map<String, Object> map) {
        if (map == null) return null;
        
        return VendorDTO.builder()
                .vendorId(map.get("vendorId") != null ? UUID.fromString(map.get("vendorId").toString()) : null)
                .tenantId(map.get("tenantId") != null ? UUID.fromString(map.get("tenantId").toString()) : null)
                .organizationId(map.get("organizationId") != null ? UUID.fromString(map.get("organizationId").toString()) : null)
                .vendorName((String) map.get("vendorName"))
                .contactName((String) map.get("contactName"))
                .contactEmail((String) map.get("contactEmail"))
                .contactPhone((String) map.get("contactPhone"))
                .address((String) map.get("address"))
                .statusId(map.get("statusId") != null ? Integer.valueOf(map.get("statusId").toString()) : null)
                .createdAt(map.get("createdAt") != null ? Instant.parse(map.get("createdAt").toString()) : null)
                .createdBy(map.get("createdBy") != null ? UUID.fromString(map.get("createdBy").toString()) : null)
                .updatedAt(map.get("updatedAt") != null ? Instant.parse(map.get("updatedAt").toString()) : null)
                .updatedBy(map.get("updatedBy") != null ? UUID.fromString(map.get("updatedBy").toString()) : null)
                .build();
    }
} 