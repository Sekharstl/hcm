package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import java.util.List;
import java.util.UUID;

public interface VendorService {
    List<VendorDTO> getAllVendors();
    PaginatedResponseDTO<VendorDTO> getAllVendorsPaginated(int page, int size, String sortBy, String sortDirection);
    VendorDTO getVendorById(UUID vendorId);
    void createVendor(VendorCreateDTO vendor);
    void updateVendor(UUID vendorId, VendorDTO vendor);
    void deleteVendor(UUID vendorId);
} 