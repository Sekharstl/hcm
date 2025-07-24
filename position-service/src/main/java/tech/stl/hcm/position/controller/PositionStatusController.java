package tech.stl.hcm.position.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.position.service.PositionStatusService;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/position-statuses")
public class PositionStatusController {
    private final PositionStatusService positionStatusService;

    public PositionStatusController(PositionStatusService positionStatusService) {
        this.positionStatusService = positionStatusService;
    }

    @GetMapping("/{id}")
    public Optional<PositionStatusDTO> getPositionStatus(@PathVariable Integer id) {
        return positionStatusService.getPositionStatusById(id);
    }

    @GetMapping
    public List<PositionStatusDTO> getAllPositionStatuses() {
        return positionStatusService.getAllPositionStatuses();
    }
}