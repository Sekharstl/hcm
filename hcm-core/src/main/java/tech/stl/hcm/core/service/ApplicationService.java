package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.ApplicationDTO;
import tech.stl.hcm.common.dto.ApplicationStatusDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationCreateDTO;
import tech.stl.hcm.common.dto.helpers.ApplicationStatusCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import java.util.List;

public interface ApplicationService {
    List<ApplicationDTO> getAllApplications();
    PaginatedResponseDTO<ApplicationDTO> getAllApplicationsPaginated(int page, int size, String sortBy, String sortDirection);
    List<ApplicationDTO> getApplicationsForCandidate(String candidateId);
    ApplicationStatusDTO getApplicationStatus(String candidateId, String applicationId);
    
    void addApplication(ApplicationCreateDTO application);
    void updateApplication(String candidateId, String applicationId, ApplicationDTO application);
    void deleteApplication(String candidateId, String applicationId);
    
    void addApplicationStatus(ApplicationStatusCreateDTO applicationStatus);
    void updateApplicationStatus(String candidateId, String applicationId, ApplicationStatusDTO applicationStatus);
    void deleteApplicationStatus(String candidateId, String applicationId);
} 