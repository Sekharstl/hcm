package tech.stl.hcm.application.service;

import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;

import java.util.List;

public interface ApplicationStatusService {
    
    ApplicationStatusDTO createApplicationStatus(ApplicationStatusCreateDTO createDTO);
    
    ApplicationStatusDTO getApplicationStatusById(Integer statusId);
    
    List<ApplicationStatusDTO> getAllApplicationStatuses();
    
    ApplicationStatusDTO updateApplicationStatus(Integer statusId, ApplicationStatusDTO dto);
    
    void deleteApplicationStatus(Integer statusId);
    
    boolean existsById(Integer statusId);
} 