package tech.stl.hcm.core.controller;

import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import tech.stl.hcm.core.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public PaginatedResponseDTO<ApplicationDTO> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "applicationId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return applicationService.getAllApplicationsPaginated(page, size, sortBy, sortDirection);
    }

    @GetMapping("/candidate/{candidateId}")
    public List<ApplicationDTO> getApplicationsForCandidate(@PathVariable String candidateId) {
        return applicationService.getApplicationsForCandidate(candidateId);
    }

    @GetMapping("/candidate/{candidateId}/application/{applicationId}/status")
    public ApplicationStatusDTO getApplicationStatus(@PathVariable String candidateId, @PathVariable String applicationId) {
        return applicationService.getApplicationStatus(candidateId, applicationId);
    }

    @PostMapping
    public void addApplication(@RequestBody ApplicationCreateDTO application) {
        applicationService.addApplication(application);
    }

    @PutMapping("/candidate/{candidateId}/application/{applicationId}")
    public void updateApplication(@PathVariable String candidateId, @PathVariable String applicationId, @RequestBody ApplicationDTO application) {
        applicationService.updateApplication(candidateId, applicationId, application);
    }

    @DeleteMapping("/candidate/{candidateId}/application/{applicationId}")
    public void deleteApplication(@PathVariable String candidateId, @PathVariable String applicationId) {
        applicationService.deleteApplication(candidateId, applicationId);
    }

    @PostMapping("/status")
    public void addApplicationStatus(@RequestBody ApplicationStatusCreateDTO applicationStatus) {
        applicationService.addApplicationStatus(applicationStatus);
    }

    @PutMapping("/candidate/{candidateId}/application/{applicationId}/status")
    public void updateApplicationStatus(@PathVariable String candidateId, @PathVariable String applicationId, @RequestBody ApplicationStatusDTO applicationStatus) {
        applicationService.updateApplicationStatus(candidateId, applicationId, applicationStatus);
    }

    @DeleteMapping("/candidate/{candidateId}/application/{applicationId}/status")
    public void deleteApplicationStatus(@PathVariable String candidateId, @PathVariable String applicationId) {
        applicationService.deleteApplicationStatus(candidateId, applicationId);
    }
} 