package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;
import java.util.List;

public interface JobRequisitionService {
    List<JobRequisitionDTO> getAllJobRequisitions();
    PaginatedResponseDTO<JobRequisitionDTO> getAllJobRequisitionsPaginated(int page, int size, String sortBy, String sortDirection);
    JobRequisitionDTO getJobRequisitionById(Integer id);
    void createJobRequisition(JobRequisitionCreateDTO createDTO);
    void updateJobRequisition(JobRequisitionDTO dto);
    void deleteJobRequisition(Integer id);
} 