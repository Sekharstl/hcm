package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.core.service.OrganizationService;
import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {
    @Mock
    private OrganizationService organizationService;
    @InjectMocks
    private OrganizationController organizationController;

    @Test
    void getAllOrganizations_delegatesToService() {
        PaginatedResponseDTO<OrganizationDTO> expectedResponse = new PaginatedResponseDTO<>();
        when(organizationService.getAllOrganizationsPaginated(0, 20, "organizationId", "ASC")).thenReturn(expectedResponse);
        organizationController.getAllOrganizations(0, 20, "organizationId", "ASC");
        verify(organizationService).getAllOrganizationsPaginated(0, 20, "organizationId", "ASC");
    }

    @Test
    void getOrganizationById_delegatesToService() {
        String id = "id";
        organizationController.getOrganizationById(id);
        verify(organizationService).getOrganizationById(id);
    }

    @Test
    void createOrganization_delegatesToService() {
        OrganizationCreateDTO dto = new OrganizationCreateDTO();
        organizationController.createOrganization(dto);
        verify(organizationService).createOrganization(dto);
    }

    @Test
    void updateOrganization_delegatesToService() {
        String id = "id";
        OrganizationDTO dto = new OrganizationDTO();
        organizationController.updateOrganization(id, dto);
        verify(organizationService).updateOrganization(id, dto);
    }

    @Test
    void deleteOrganization_delegatesToService() {
        String id = "id";
        organizationController.deleteOrganization(id);
        verify(organizationService).deleteOrganization(id);
    }

    @Test
    void getOrganizationById_whenServiceThrows_shouldPropagate() {
        String id = "id";
        when(organizationService.getOrganizationById(id)).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> organizationController.getOrganizationById(id));
    }

    @Test
    void createOrganization_withNull_shouldCallService() {
        organizationController.createOrganization(null);
        verify(organizationService).createOrganization(null);
    }
} 