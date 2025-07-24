package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.PositionService;
import java.util.List;

public interface PositionService {
    List<PositionDTO> getAllPositions();
    PaginatedResponseDTO<PositionDTO> getAllPositionsPaginated(int page, int size, String sortBy, String sortDirection);
    PositionDTO getPositionById(Integer id);
    void createPosition(PositionCreateDTO position);
    void updatePosition(Integer id, PositionDTO position);
    void deletePosition(Integer id);
    
    // Position Status endpoints
    List<PositionStatusDTO> getAllPositionStatuses();
    PositionStatusDTO getPositionStatusById(Integer id);
    void createPositionStatus(PositionStatusDTO status);
    void updatePositionStatus(Integer id, PositionStatusDTO status);
    void deletePositionStatus(Integer id);
} 