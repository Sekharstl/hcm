package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.core.service.TenantService;
import tech.stl.hcm.common.dto.TenantDTO;
import tech.stl.hcm.common.dto.helpers.TenantCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantControllerTest {
    @Mock
    private TenantService tenantService;
    @InjectMocks
    private TenantController tenantController;

    @Test
    void getAllTenants_delegatesToService() {
        // Given
        TenantDTO tenant1 = TenantDTO.builder()
            .tenantId(UUID.randomUUID())
            .name("Tech Solutions Inc")
            .domain("techsolutions.com")
            .build();
        
        TenantDTO tenant2 = TenantDTO.builder()
            .tenantId(UUID.randomUUID())
            .name("Global Services Ltd")
            .domain("globalservices.com")
            .build();
        
        PaginatedResponseDTO<TenantDTO> expectedResponse = new PaginatedResponseDTO<>();
        expectedResponse.setContent(List.of(tenant1, tenant2));
        expectedResponse.setTotalElements(2);
        expectedResponse.setTotalPages(1);
        expectedResponse.setPageNumber(0);
        expectedResponse.setPageSize(20);
        
        when(tenantService.getAllTenantsPaginated(0, 20, "tenantId", "ASC")).thenReturn(expectedResponse);
        
        // When
        PaginatedResponseDTO<TenantDTO> result = tenantController.getAllTenants(0, 20, "tenantId", "ASC");
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(tenant1.getName(), result.getContent().get(0).getName());
        assertEquals(tenant2.getName(), result.getContent().get(1).getName());
        verify(tenantService).getAllTenantsPaginated(0, 20, "tenantId", "ASC");
    }

    @Test
    void getTenantById_delegatesToService() {
        String id = "id";
        tenantController.getTenantById(id);
        verify(tenantService).getTenantById(id);
    }

    @Test
    void createTenant_delegatesToService() {
        // Given
        TenantCreateDTO dto = TenantCreateDTO.builder()
            .name("Tech Solutions Inc")
            .domain("techsolutions.com")
            .build();
        
        // When
        tenantController.createTenant(dto);
        
        // Then
        verify(tenantService).createTenant(dto);
    }

    @Test
    void updateTenant_delegatesToService() {
        // Given
        String id = "tenant-123";
        TenantDTO dto = TenantDTO.builder()
            .tenantId(UUID.randomUUID())
            .name("Updated Tech Solutions Inc")
            .domain("updated-techsolutions.com")
            .build();
        
        // When
        tenantController.updateTenant(id, dto);
        
        // Then
        verify(tenantService).updateTenant(id, dto);
    }

    @Test
    void deleteTenant_delegatesToService() {
        String id = "id";
        tenantController.deleteTenant(id);
        verify(tenantService).deleteTenant(id);
    }

    @Test
    void getTenantById_whenServiceThrows_shouldPropagate() {
        String id = "id";
        when(tenantService.getTenantById(id)).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> tenantController.getTenantById(id));
    }

    @Test
    void createTenant_withNull_shouldCallService() {
        tenantController.createTenant(null);
        verify(tenantService).createTenant(null);
    }
} 