package tech.stl.hcm.position.service;

import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionStatusCreateDTO;
import java.util.List;
import java.util.Optional;

public interface PositionStatusService {
    PositionStatusDTO createPositionStatus(PositionStatusCreateDTO statusCreateDTO);
    PositionStatusDTO updatePositionStatus(PositionStatusDTO statusDTO);
    void deletePositionStatus(Integer statusId);
    Optional<PositionStatusDTO> getPositionStatusById(Integer statusId);
    List<PositionStatusDTO> getAllPositionStatuses();
} 