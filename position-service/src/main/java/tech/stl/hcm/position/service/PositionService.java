package tech.stl.hcm.position.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import java.util.List;
import java.util.Optional;

public interface PositionService {
    PositionDTO createPosition(PositionCreateDTO positionCreateDTO);
    PositionDTO updatePosition(PositionDTO positionDTO);
    void deletePosition(Integer positionId);
    Optional<PositionDTO> getPositionById(Integer positionId);
    List<PositionDTO> getAllPositions();
    Page<PositionDTO> getAllPositionsPaginated(Pageable pageable);
} 