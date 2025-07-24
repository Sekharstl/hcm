package tech.stl.hcm.vendor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorService {
    List<VendorDTO> getAllVendors();
    Page<VendorDTO> getAllVendorsPaginated(Pageable pageable);
    Optional<VendorDTO> getVendorById(UUID vendorId);
    VendorDTO createVendor(VendorCreateDTO createDTO);
    void deleteVendor(UUID vendorId);
    VendorDTO updateVendor(VendorDTO vendorDTO);
} 