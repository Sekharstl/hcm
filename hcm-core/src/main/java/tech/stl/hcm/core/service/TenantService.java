package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.TenantDTO;
import tech.stl.hcm.common.dto.helpers.TenantCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import java.util.List;

public interface TenantService {
    List<TenantDTO> getAllTenants();
    PaginatedResponseDTO<TenantDTO> getAllTenantsPaginated(int page, int size, String sortBy, String sortDirection);
    TenantDTO getTenantById(String tenantId);
    void createTenant(TenantCreateDTO tenant);
    void updateTenant(String tenantId, TenantDTO tenant);
    void deleteTenant(String tenantId);
} 