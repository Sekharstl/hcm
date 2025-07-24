package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.JobRequisitionService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/job-requisitions")
@RequiredArgsConstructor
public class JobRequisitionController {
    private final JobRequisitionService jobRequisitionService;

    @GetMapping
    public PaginatedResponseDTO<JobRequisitionDTO> getAllJobRequisitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "jobRequisitionId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return jobRequisitionService.getAllJobRequisitionsPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public JobRequisitionDTO getJobRequisitionById(@PathVariable Integer id) {
        return jobRequisitionService.getJobRequisitionById(id);
    }

    @PostMapping
    public void createJobRequisition(@RequestBody JobRequisitionCreateDTO createDTO) {
        jobRequisitionService.createJobRequisition(createDTO);
    }

    @PutMapping
    public void updateJobRequisition(@RequestBody JobRequisitionDTO dto) {
        jobRequisitionService.updateJobRequisition(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteJobRequisition(@PathVariable Integer id) {
        jobRequisitionService.deleteJobRequisition(id);
    }
} 