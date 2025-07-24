package tech.stl.hcm.position.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.PositionStatus;
import tech.stl.hcm.common.db.repositories.PositionStatusRepository;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionStatusCreateDTO;
import tech.stl.hcm.common.mapper.PositionStatusMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionStatusServiceImpl implements PositionStatusService {
    private final PositionStatusRepository positionStatusRepository;

    @Override
    @Transactional
    public PositionStatusDTO createPositionStatus(PositionStatusCreateDTO statusCreateDTO) {
        PositionStatus entity = PositionStatusMapper.toEntity(statusCreateDTO);
        return PositionStatusMapper.toDTO(positionStatusRepository.save(entity));
    }

    @Override
    @Transactional
    public PositionStatusDTO updatePositionStatus(PositionStatusDTO statusDTO) {
        Optional<PositionStatus> optional = positionStatusRepository.findById(statusDTO.getStatusId());
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("PositionStatus not found");
        }
        PositionStatus entity = optional.get();
        entity.setName(statusDTO.getName());
        entity.setDescription(statusDTO.getDescription());
        entity.setUpdatedAt(statusDTO.getUpdatedAt());
        entity.setUpdatedBy(statusDTO.getUpdatedBy());
        return PositionStatusMapper.toDTO(positionStatusRepository.save(entity));
    }

    @Override
    @Transactional
    public void deletePositionStatus(Integer statusId) {
        positionStatusRepository.deleteById(statusId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PositionStatusDTO> getPositionStatusById(Integer statusId) {
        return positionStatusRepository.findById(statusId).map(PositionStatusMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionStatusDTO> getAllPositionStatuses() {
        return positionStatusRepository.findAll().stream().map(PositionStatusMapper::toDTO).collect(Collectors.toList());
    }
} 