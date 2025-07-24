package tech.stl.hcm.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.common.dto.ApplicationDTO;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    
    ApplicationDTO createApplication(ApplicationCreateDTO createDTO);
    
    ApplicationDTO getApplicationById(Integer applicationId);
    
    List<ApplicationDTO> getAllApplications();
    
    Page<ApplicationDTO> getAllApplicationsPaginated(Pageable pageable);
    
    List<ApplicationDTO> getApplicationsByCandidateId(UUID candidateId);
    
    List<ApplicationDTO> getApplicationsByRequisitionId(Integer requisitionId);
    
    ApplicationDTO updateApplication(Integer applicationId, ApplicationDTO dto);
    
    void deleteApplication(Integer applicationId);
    
    boolean existsById(Integer applicationId);
} 