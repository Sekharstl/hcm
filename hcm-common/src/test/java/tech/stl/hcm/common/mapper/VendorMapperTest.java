package tech.stl.hcm.common.mapper;

import org.junit.jupiter.api.Test;
import tech.stl.hcm.common.db.entities.Vendor;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VendorMapperTest {

    private final UUID vendorId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();
    private final UUID organizationId = UUID.randomUUID();
    private final UUID createdBy = UUID.randomUUID();
    private final Instant now = Instant.now();

    @Test
    void toDTO_shouldMapEntityToDTO() {
        // Given
        Vendor entity = Vendor.builder()
            .vendorId(vendorId)
            .tenantId(tenantId)
            .organizationId(organizationId)
            .vendorName("Tech Solutions Inc")
            .contactName("John Doe")
            .contactEmail("john.doe@techsolutions.com")
            .contactPhone("+1-555-0123")
            .address("123 Tech Street, Silicon Valley, CA")
            .statusId(1)
            .createdAt(now)
            .createdBy(createdBy)
            .updatedAt(now)
            .updatedBy(createdBy)
            .build();

        // When
        VendorDTO result = VendorMapper.toDTO(entity);

        // Then
        assertNotNull(result);
        assertEquals(entity.getVendorId(), result.getVendorId());
        assertEquals(entity.getTenantId(), result.getTenantId());
        assertEquals(entity.getOrganizationId(), result.getOrganizationId());
        assertEquals(entity.getVendorName(), result.getVendorName());
        assertEquals(entity.getContactName(), result.getContactName());
        assertEquals(entity.getContactEmail(), result.getContactEmail());
        assertEquals(entity.getContactPhone(), result.getContactPhone());
        assertEquals(entity.getAddress(), result.getAddress());
        assertEquals(entity.getStatusId(), result.getStatusId());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        assertEquals(entity.getCreatedBy(), result.getCreatedBy());
        assertEquals(entity.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(entity.getUpdatedBy(), result.getUpdatedBy());
    }

    @Test
    void toDTO_withNullEntity_shouldReturnNull() {
        // When
        VendorDTO result = VendorMapper.toDTO(null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntity_shouldMapCreateDTOToEntity() {
        // Given
        VendorCreateDTO dto = VendorCreateDTO.builder()
            .tenantId(tenantId)
            .organizationId(organizationId)
            .vendorName("Tech Solutions Inc")
            .contactName("John Doe")
            .contactEmail("john.doe@techsolutions.com")
            .contactPhone("+1-555-0123")
            .address("123 Tech Street, Silicon Valley, CA")
            .statusId(1)
            .createdBy(createdBy)
            .build();

        // When
        Vendor result = VendorMapper.toEntity(dto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getVendorId()); // Should generate new UUID
        assertEquals(dto.getTenantId(), result.getTenantId());
        assertEquals(dto.getOrganizationId(), result.getOrganizationId());
        assertEquals(dto.getVendorName(), result.getVendorName());
        assertEquals(dto.getContactName(), result.getContactName());
        assertEquals(dto.getContactEmail(), result.getContactEmail());
        assertEquals(dto.getContactPhone(), result.getContactPhone());
        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getStatusId(), result.getStatusId());
        assertNotNull(result.getCreatedAt()); // Should set current time
        assertEquals(dto.getCreatedBy(), result.getCreatedBy());
        assertNotNull(result.getUpdatedAt()); // Should set current time
        assertEquals(dto.getCreatedBy(), result.getUpdatedBy());
    }

    @Test
    void toEntity_withNullDTO_shouldReturnNull() {
        // When
        Vendor result = VendorMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntity_shouldGenerateNewVendorId() {
        // Given
        VendorCreateDTO dto = VendorCreateDTO.builder()
            .vendorName("Tech Solutions Inc")
            .build();

        // When
        Vendor result1 = VendorMapper.toEntity(dto);
        Vendor result2 = VendorMapper.toEntity(dto);

        // Then
        assertNotNull(result1.getVendorId());
        assertNotNull(result2.getVendorId());
        assertNotEquals(result1.getVendorId(), result2.getVendorId()); // Should be different UUIDs
    }
} 