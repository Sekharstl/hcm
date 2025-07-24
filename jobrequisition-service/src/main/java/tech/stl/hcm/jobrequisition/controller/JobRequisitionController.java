package tech.stl.hcm.jobrequisition.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.jobrequisition.service.JobRequisitionService;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job-requisitions")
public class JobRequisitionController {
    private final JobRequisitionService jobRequisitionService;

    public JobRequisitionController(JobRequisitionService jobRequisitionService) {
        this.jobRequisitionService = jobRequisitionService;
    }

    @GetMapping("/{id}")
    public Optional<JobRequisitionDTO> getJobRequisition(@PathVariable Integer id) {
        return jobRequisitionService.getJobRequisitionById(id);
    }

    @GetMapping
    public Page<JobRequisitionDTO> getAllJobRequisitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "jobRequisitionId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return jobRequisitionService.getAllJobRequisitionsPaginated(pageable);
    }
} 