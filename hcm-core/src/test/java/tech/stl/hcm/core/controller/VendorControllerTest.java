package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.core.service.VendorService;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {
    @Mock
    private VendorService vendorService;
    @InjectMocks
    private VendorController vendorController;

    @Test
    void getAllVendors_delegatesToService() {
        // Given
        VendorDTO vendor1 = VendorDTO.builder()
            .vendorId(UUID.randomUUID())
            .vendorName("Tech Solutions Inc")
            .contactEmail("contact@techsolutions.com")
            .contactPhone("+1-555-0123")
            .build();
        
        VendorDTO vendor2 = VendorDTO.builder()
            .vendorId(UUID.randomUUID())
            .vendorName("Global Services Ltd")
            .contactEmail("info@globalservices.com")
            .contactPhone("+1-555-0456")
            .build();
        
        PaginatedResponseDTO<VendorDTO> expectedResponse = new PaginatedResponseDTO<>();
        expectedResponse.setContent(List.of(vendor1, vendor2));
        expectedResponse.setTotalElements(2);
        expectedResponse.setTotalPages(1);
        expectedResponse.setPageNumber(0);
        expectedResponse.setPageSize(20);
        
        when(vendorService.getAllVendorsPaginated(0, 20, "vendorId", "ASC")).thenReturn(expectedResponse);
        
        // When
        PaginatedResponseDTO<VendorDTO> result = vendorController.getAllVendors(0, 20, "vendorId", "ASC");
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(vendor1.getVendorName(), result.getContent().get(0).getVendorName());
        assertEquals(vendor2.getVendorName(), result.getContent().get(1).getVendorName());
        verify(vendorService).getAllVendorsPaginated(0, 20, "vendorId", "ASC");
    }

    @Test
    void getVendorById_delegatesToService() {
        // Given
        UUID id = UUID.randomUUID();
        VendorDTO expectedVendor = VendorDTO.builder()
            .vendorId(id)
            .vendorName("Tech Solutions Inc")
            .contactEmail("contact@techsolutions.com")
            .contactPhone("+1-555-0123")
            .build();
        
        when(vendorService.getVendorById(id)).thenReturn(expectedVendor);
        
        // When
        vendorController.getVendorById(id);
        
        // Then
        verify(vendorService).getVendorById(id);
    }

    @Test
    void createVendor_delegatesToService() {
        // Given
        VendorCreateDTO dto = VendorCreateDTO.builder()
            .vendorName("Tech Solutions Inc")
            .contactEmail("contact@techsolutions.com")
            .contactPhone("+1-555-0123")
            .build();
        
        // When
        vendorController.createVendor(dto);
        
        // Then
        verify(vendorService).createVendor(dto);
    }

    @Test
    void updateVendor_delegatesToService() {
        // Given
        UUID id = UUID.randomUUID();
        VendorDTO dto = VendorDTO.builder()
            .vendorId(id)
            .vendorName("Updated Tech Solutions Inc")
            .contactEmail("updated@techsolutions.com")
            .contactPhone("+1-555-9999")
            .build();
        
        // When
        vendorController.updateVendor(id, dto);
        
        // Then
        verify(vendorService).updateVendor(id, dto);
    }

    @Test
    void deleteVendor_delegatesToService() {
        UUID id = UUID.randomUUID();
        vendorController.deleteVendor(id);
        verify(vendorService).deleteVendor(id);
    }

    @Test
    void getVendorById_whenServiceThrows_shouldPropagate() {
        UUID id = UUID.randomUUID();
        when(vendorService.getVendorById(id)).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> vendorController.getVendorById(id));
    }

    @Test
    void createVendor_withNull_shouldCallService() {
        vendorController.createVendor(null);
        verify(vendorService).createVendor(null);
    }
} 