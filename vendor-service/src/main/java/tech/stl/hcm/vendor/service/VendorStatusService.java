package tech.stl.hcm.vendor.service;

import tech.stl.hcm.common.dto.VendorStatusDTO;
import tech.stl.hcm.common.dto.helpers.VendorStatusCreateDTO;
import java.util.List;
import java.util.Optional;

public interface VendorStatusService {
    VendorStatusDTO createVendorStatus(VendorStatusCreateDTO statusCreateDTO);
    void deleteVendorStatus(Integer statusId);
    Optional<VendorStatusDTO> getVendorStatusById(Integer statusId);
    List<VendorStatusDTO> getAllVendorStatuses();
    VendorStatusDTO updateVendorStatus(VendorStatusDTO statusDTO);
} 