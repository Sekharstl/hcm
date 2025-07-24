package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public PaginatedResponseDTO<OrganizationDTO> getAllOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "organizationId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return organizationService.getAllOrganizationsPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{organizationId}")
    public OrganizationDTO getOrganizationById(@PathVariable String organizationId) {
        return organizationService.getOrganizationById(organizationId);
    }

    @PostMapping
    public void createOrganization(@RequestBody OrganizationCreateDTO organization) {
        organizationService.createOrganization(organization);
    }

    @PutMapping("/{organizationId}")
    public void updateOrganization(@PathVariable String organizationId, @RequestBody OrganizationDTO organization) {
        organizationService.updateOrganization(organizationId, organization);
    }

    @DeleteMapping("/{organizationId}")
    public void deleteOrganization(@PathVariable String organizationId) {
        organizationService.deleteOrganization(organizationId);
    }
} 