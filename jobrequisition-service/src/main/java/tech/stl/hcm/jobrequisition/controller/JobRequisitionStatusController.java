package tech.stl.hcm.jobrequisition.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.jobrequisition.service.JobRequisitionStatusService;
import tech.stl.hcm.common.dto.JobRequisitionStatusDTO;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job-requisition-statuses")
public class JobRequisitionStatusController {
    private final JobRequisitionStatusService jobRequisitionStatusService;

    public JobRequisitionStatusController(JobRequisitionStatusService jobRequisitionStatusService) {
        this.jobRequisitionStatusService = jobRequisitionStatusService;
    }

    @GetMapping("/{id}")
    public Optional<JobRequisitionStatusDTO> getJobRequisitionStatus(@PathVariable Integer id) {
        return jobRequisitionStatusService.getJobRequisitionStatusById(id);
    }

    @GetMapping
    public List<JobRequisitionStatusDTO> getAllJobRequisitionStatuses() {
        return jobRequisitionStatusService.getAllJobRequisitionStatuses();
    }
} 