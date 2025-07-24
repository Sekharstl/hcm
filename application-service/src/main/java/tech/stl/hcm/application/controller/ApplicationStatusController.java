package tech.stl.hcm.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.application.service.ApplicationStatusService;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;

import java.util.List;

@RestController
@RequestMapping("/application-statuses")
@RequiredArgsConstructor
@Slf4j
public class ApplicationStatusController {

    private final ApplicationStatusService applicationStatusService;

    @GetMapping("/{statusId}")
    public ResponseEntity<ApplicationStatusDTO> getApplicationStatusById(@PathVariable Integer statusId) {
        log.info("Received request to get application status by ID: {}", statusId);
        
        ApplicationStatusDTO applicationStatus = applicationStatusService.getApplicationStatusById(statusId);
        return ResponseEntity.ok(applicationStatus);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationStatusDTO>> getAllApplicationStatuses() {
        log.info("Received request to get all application statuses");
        
        List<ApplicationStatusDTO> applicationStatuses = applicationStatusService.getAllApplicationStatuses();
        return ResponseEntity.ok(applicationStatuses);
    }
} 