package tech.stl.hcm.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.application.exception.ApplicationNotFoundException;
import tech.stl.hcm.common.db.entities.Application;
import tech.stl.hcm.common.db.repositories.ApplicationRepository;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.common.mapper.ApplicationMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public ApplicationDTO createApplication(ApplicationCreateDTO createDTO) {
        log.info("Creating application for candidate: {} and requisition: {}", 
                createDTO.getCandidateId(), createDTO.getRequisitionId());
        Application application = ApplicationMapper.toEntity(createDTO);
        Application savedApplication = applicationRepository.save(application);
        log.info("Application created with ID: {}", savedApplication.getApplicationId());
        return ApplicationMapper.toDTO(savedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDTO getApplicationById(Integer applicationId) {
        log.info("Fetching application by ID: {}", applicationId);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(
                        "Application not found with ID: " + applicationId));
        return ApplicationMapper.toDTO(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getAllApplications() {
        log.info("Fetching all applications");
        return applicationRepository.findAll().stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getAllApplicationsPaginated(Pageable pageable) {
        log.info("Fetching applications with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return applicationRepository.findAll(pageable)
                .map(ApplicationMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByCandidateId(UUID candidateId) {
        log.info("Fetching applications for candidate: {}", candidateId);
        return applicationRepository.findByCandidateId(candidateId).stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByRequisitionId(Integer requisitionId) {
        log.info("Fetching applications for requisition: {}", requisitionId);
        return applicationRepository.findByRequisitionId(requisitionId).stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDTO updateApplication(Integer applicationId, ApplicationDTO dto) {
        log.info("Updating application with ID: {}", applicationId);
        Application existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(
                        "Application not found with ID: " + applicationId));
        ApplicationMapper.updateEntity(existingApplication, dto);
        Application updatedApplication = applicationRepository.save(existingApplication);
        log.info("Application updated successfully with ID: {}", applicationId);
        return ApplicationMapper.toDTO(updatedApplication);
    }

    @Override
    public void deleteApplication(Integer applicationId) {
        log.info("Deleting application with ID: {}", applicationId);
        if (!applicationRepository.existsById(applicationId)) {
            throw new ApplicationNotFoundException("Application not found with ID: " + applicationId);
        }
        applicationRepository.deleteById(applicationId);
        log.info("Application deleted successfully with ID: {}", applicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer applicationId) {
        return applicationRepository.existsById(applicationId);
    }
} 