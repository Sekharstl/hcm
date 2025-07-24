package tech.stl.hcm.vendor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.vendor.service.VendorService;
import tech.stl.hcm.vendor.service.VendorStatusService;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.dto.VendorStatusDTO;
import tech.stl.hcm.common.dto.helpers.VendorStatusCreateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;
    private final VendorStatusService vendorStatusService;

    @GetMapping
    public ResponseEntity<Page<VendorDTO>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "vendorId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<VendorDTO> vendors = vendorService.getAllVendorsPaginated(pageable);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable UUID vendorId) {
        return vendorService.getVendorById(vendorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorCreateDTO createDTO) {
        VendorDTO created = vendorService.createVendor(createDTO);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<Void> deleteVendor(@PathVariable UUID vendorId) {
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.noContent().build();
    }

    // Vendor Status Endpoints
    @GetMapping("/statuses")
    public List<VendorStatusDTO> getAllVendorStatuses() {
        return vendorStatusService.getAllVendorStatuses();
    }

    @GetMapping("/statuses/{statusId}")
    public ResponseEntity<VendorStatusDTO> getVendorStatusById(@PathVariable Integer statusId) {
        return vendorStatusService.getVendorStatusById(statusId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/statuses")
    public ResponseEntity<VendorStatusDTO> createVendorStatus(@RequestBody VendorStatusCreateDTO createDTO) {
        VendorStatusDTO created = vendorStatusService.createVendorStatus(createDTO);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/statuses/{statusId}")
    public ResponseEntity<Void> deleteVendorStatus(@PathVariable Integer statusId) {
        vendorStatusService.deleteVendorStatus(statusId);
        return ResponseEntity.noContent().build();
    }
} 