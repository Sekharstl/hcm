package tech.stl.hcm.vendor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.Vendor;
import tech.stl.hcm.common.db.repositories.VendorRepository;
import tech.stl.hcm.common.dto.VendorDTO;
import tech.stl.hcm.common.dto.helpers.VendorCreateDTO;
import tech.stl.hcm.common.mapper.VendorMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    @Override
    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(VendorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<VendorDTO> getAllVendorsPaginated(Pageable pageable) {
        return vendorRepository.findAll(pageable)
                .map(VendorMapper::toDTO);
    }

    @Override
    public Optional<VendorDTO> getVendorById(UUID vendorId) {
        return vendorRepository.findById(vendorId).map(VendorMapper::toDTO);
    }

    @Override
    @Transactional
    public VendorDTO createVendor(VendorCreateDTO createDTO) {
        Vendor vendor = VendorMapper.toEntity(createDTO);
        vendor = vendorRepository.save(vendor);
        return VendorMapper.toDTO(vendor);
    }

    @Override
    @Transactional
    public void deleteVendor(UUID vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    @Override
    @Transactional
    public VendorDTO updateVendor(VendorDTO vendorDTO) {
        return vendorRepository.findById(vendorDTO.getVendorId())
                .map(entity -> {
                    VendorMapper.updateEntity(entity, vendorDTO);
                    return VendorMapper.toDTO(vendorRepository.save(entity));
                })
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
    }
} 