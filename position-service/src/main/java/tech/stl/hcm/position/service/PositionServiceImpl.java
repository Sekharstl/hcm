package tech.stl.hcm.position.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.Position;
import tech.stl.hcm.common.db.repositories.PositionRepository;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.common.mapper.PositionMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

    @Override
    @Transactional
    public PositionDTO createPosition(PositionCreateDTO positionCreateDTO) {
        Position entity = PositionMapper.toEntity(positionCreateDTO);
        return PositionMapper.toDTO(positionRepository.save(entity));
    }

    @Override
    @Transactional
    public PositionDTO updatePosition(PositionDTO positionDTO) {
        Optional<Position> optional = positionRepository.findById(positionDTO.getPositionId());
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Position not found");
        }
        Position entity = optional.get();
        entity.setTitle(positionDTO.getTitle());
        entity.setLocation(positionDTO.getLocation());
        entity.setDescription(positionDTO.getDescription());
        entity.setEmploymentType(positionDTO.getEmploymentType());
        entity.setStatusId(positionDTO.getStatusId());
        entity.setHeadcount(positionDTO.getHeadcount());
        entity.setUpdatedAt(positionDTO.getUpdatedAt());
        entity.setUpdatedBy(positionDTO.getUpdatedBy());
        return PositionMapper.toDTO(positionRepository.save(entity));
    }

    @Override
    @Transactional
    public void deletePosition(Integer positionId) {
        positionRepository.deleteById(positionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PositionDTO> getPositionById(Integer positionId) {
        return positionRepository.findById(positionId).map(PositionMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionDTO> getAllPositions() {
        return positionRepository.findAll().stream().map(PositionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PositionDTO> getAllPositionsPaginated(Pageable pageable) {
        return positionRepository.findAll(pageable)
                .map(PositionMapper::toDTO);
    }
} 