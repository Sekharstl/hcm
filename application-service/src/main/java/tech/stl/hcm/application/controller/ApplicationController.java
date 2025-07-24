package tech.stl.hcm.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.application.service.ApplicationService;
import tech.stl.hcm.common.dto.ApplicationDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Integer applicationId) {
        log.info("Received request to get application by ID: {}", applicationId);
        
        ApplicationDTO application = applicationService.getApplicationById(applicationId);
        return ResponseEntity.ok(application);
    }

    @GetMapping
    public ResponseEntity<Page<ApplicationDTO>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "applicationId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("Received request to get applications with pagination: page={}, size={}, sortBy={}, sortDirection={}", 
                page, size, sortBy, sortDirection);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ApplicationDTO> applications = applicationService.getAllApplicationsPaginated(pageable);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByCandidateId(@PathVariable UUID candidateId) {
        log.info("Received request to get applications for candidate: {}", candidateId);
        
        List<ApplicationDTO> applications = applicationService.getApplicationsByCandidateId(candidateId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/requisition/{requisitionId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByRequisitionId(@PathVariable Integer requisitionId) {
        log.info("Received request to get applications for requisition: {}", requisitionId);
        
        List<ApplicationDTO> applications = applicationService.getApplicationsByRequisitionId(requisitionId);
        return ResponseEntity.ok(applications);
    }
} 