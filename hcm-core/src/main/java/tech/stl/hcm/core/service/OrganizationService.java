package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import java.util.List;

public interface OrganizationService {
    List<OrganizationDTO> getAllOrganizations();
    PaginatedResponseDTO<OrganizationDTO> getAllOrganizationsPaginated(int page, int size, String sortBy, String sortDirection);
    OrganizationDTO getOrganizationById(String organizationId);
    void createOrganization(OrganizationCreateDTO organization);
    void updateOrganization(String organizationId, OrganizationDTO organization);
    void deleteOrganization(String organizationId);
} 