package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.TenantDTO;
import tech.stl.hcm.common.dto.helpers.TenantCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.TenantService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @GetMapping
    public PaginatedResponseDTO<TenantDTO> getAllTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "tenantId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return tenantService.getAllTenantsPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{tenantId}")
    public TenantDTO getTenantById(@PathVariable String tenantId) {
        return tenantService.getTenantById(tenantId);
    }

    @PostMapping
    public void createTenant(@RequestBody TenantCreateDTO tenant) {
        tenantService.createTenant(tenant);
    }

    @PutMapping("/{tenantId}")
    public void updateTenant(@PathVariable String tenantId, @RequestBody TenantDTO tenant) {
        tenantService.updateTenant(tenantId, tenant);
    }

    @DeleteMapping("/{tenantId}")
    public void deleteTenant(@PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
    }
} 