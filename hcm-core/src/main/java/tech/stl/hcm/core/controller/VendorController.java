package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.VendorService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @GetMapping
    public PaginatedResponseDTO<VendorDTO> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "vendorId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return vendorService.getAllVendorsPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{vendorId}")
    public void getVendorById(@PathVariable UUID vendorId) {
        vendorService.getVendorById(vendorId);
    }

    @PostMapping
    public void createVendor(@RequestBody VendorCreateDTO vendor) {
        vendorService.createVendor(vendor);
    }

    @PutMapping("/{vendorId}")
    public void updateVendor(@PathVariable UUID vendorId, @RequestBody VendorDTO vendor) {
        vendorService.updateVendor(vendorId, vendor);
    }

    @DeleteMapping("/{vendorId}")
    public void deleteVendor(@PathVariable UUID vendorId) {
        vendorService.deleteVendor(vendorId);
    }
} 