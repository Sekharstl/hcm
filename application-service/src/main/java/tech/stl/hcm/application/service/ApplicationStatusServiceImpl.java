package tech.stl.hcm.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.application.exception.ApplicationStatusNotFoundException;
import tech.stl.hcm.common.db.entities.ApplicationStatus;
import tech.stl.hcm.common.db.repositories.ApplicationStatusRepository;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.common.mapper.ApplicationStatusMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationStatusServiceImpl implements ApplicationStatusService {

    private final ApplicationStatusRepository applicationStatusRepository;

    @Override
    public ApplicationStatusDTO createApplicationStatus(ApplicationStatusCreateDTO createDTO) {
        log.info("Creating application status: {}", createDTO.getName());
        ApplicationStatus applicationStatus = ApplicationStatusMapper.toEntity(createDTO);
        ApplicationStatus savedApplicationStatus = applicationStatusRepository.save(applicationStatus);
        log.info("Application status created with ID: {}", savedApplicationStatus.getStatusId());
        return ApplicationStatusMapper.toDTO(savedApplicationStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationStatusDTO getApplicationStatusById(Integer statusId) {
        log.info("Fetching application status by ID: {}", statusId);
        ApplicationStatus applicationStatus = applicationStatusRepository.findById(statusId)
                .orElseThrow(() -> new ApplicationStatusNotFoundException(
                        "Application status not found with ID: " + statusId));
        return ApplicationStatusMapper.toDTO(applicationStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationStatusDTO> getAllApplicationStatuses() {
        log.info("Fetching all application statuses");
        return applicationStatusRepository.findAll().stream()
                .map(ApplicationStatusMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationStatusDTO updateApplicationStatus(Integer statusId, ApplicationStatusDTO dto) {
        log.info("Updating application status with ID: {}", statusId);
        ApplicationStatus existingApplicationStatus = applicationStatusRepository.findById(statusId)
                .orElseThrow(() -> new ApplicationStatusNotFoundException(
                        "Application status not found with ID: " + statusId));
        ApplicationStatusMapper.updateEntity(existingApplicationStatus, dto);
        ApplicationStatus updatedApplicationStatus = applicationStatusRepository.save(existingApplicationStatus);
        log.info("Application status updated successfully with ID: {}", statusId);
        return ApplicationStatusMapper.toDTO(updatedApplicationStatus);
    }

    @Override
    public void deleteApplicationStatus(Integer statusId) {
        log.info("Deleting application status with ID: {}", statusId);
        if (!applicationStatusRepository.existsById(statusId)) {
            throw new ApplicationStatusNotFoundException("Application status not found with ID: " + statusId);
        }
        applicationStatusRepository.deleteById(statusId);
        log.info("Application status deleted successfully with ID: {}", statusId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer statusId) {
        return applicationStatusRepository.existsById(statusId);
    }
} 