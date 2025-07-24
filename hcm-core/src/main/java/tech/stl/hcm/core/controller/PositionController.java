package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.PositionService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @GetMapping
    public PaginatedResponseDTO<PositionDTO> getAllPositions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "positionId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return positionService.getAllPositionsPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public PositionDTO getPositionById(@PathVariable Integer id) {
        return positionService.getPositionById(id);
    }

    @PostMapping
    public void createPosition(@RequestBody PositionCreateDTO position) {
        positionService.createPosition(position);
    }

    @PutMapping("/{id}")
    public void updatePosition(@PathVariable Integer id, @RequestBody PositionDTO position) {
        positionService.updatePosition(id, position);
    }

    @DeleteMapping("/{id}")
    public void deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
    }

    // Position Status endpoints
    @GetMapping("/statuses")
    public List<PositionStatusDTO> getAllPositionStatuses() {
        return positionService.getAllPositionStatuses();
    }

    @GetMapping("/statuses/{id}")
    public PositionStatusDTO getPositionStatusById(@PathVariable Integer id) {
        return positionService.getPositionStatusById(id);
    }

    @PostMapping("/statuses")
    public void createPositionStatus(@RequestBody PositionStatusDTO status) {
        positionService.createPositionStatus(status);
    }

    @PutMapping("/statuses/{id}")
    public void updatePositionStatus(@PathVariable Integer id, @RequestBody PositionStatusDTO status) {
        positionService.updatePositionStatus(id, status);
    }

    @DeleteMapping("/statuses/{id}")
    public void deletePositionStatus(@PathVariable Integer id) {
        positionService.deletePositionStatus(id);
    }
} 