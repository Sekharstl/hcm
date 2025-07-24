package tech.stl.hcm.vendor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.VendorStatus;
import tech.stl.hcm.common.db.repositories.VendorStatusRepository;
import tech.stl.hcm.common.dto.VendorStatusDTO;
import tech.stl.hcm.common.dto.helpers.VendorStatusCreateDTO;
import tech.stl.hcm.common.mapper.VendorStatusMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorStatusServiceImpl implements VendorStatusService {
    private final VendorStatusRepository vendorStatusRepository;

    @Override
    @Transactional
    public VendorStatusDTO createVendorStatus(VendorStatusCreateDTO statusCreateDTO) {
        VendorStatus entity = VendorStatusMapper.toEntity(statusCreateDTO);
        return VendorStatusMapper.toDTO(vendorStatusRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteVendorStatus(Integer statusId) {
        vendorStatusRepository.deleteById(statusId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VendorStatusDTO> getVendorStatusById(Integer statusId) {
        return vendorStatusRepository.findById(statusId).map(VendorStatusMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorStatusDTO> getAllVendorStatuses() {
        return vendorStatusRepository.findAll().stream().map(VendorStatusMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VendorStatusDTO updateVendorStatus(VendorStatusDTO statusDTO) {
        return vendorStatusRepository.findById(statusDTO.getStatusId())
                .map(entity -> {
                    entity.setName(statusDTO.getName());
                    entity.setUpdatedAt(statusDTO.getUpdatedAt());
                    entity.setUpdatedBy(statusDTO.getUpdatedBy());
                    return VendorStatusMapper.toDTO(vendorStatusRepository.save(entity));
                })
                .orElseThrow(() -> new IllegalArgumentException("VendorStatus not found"));
    }
} 