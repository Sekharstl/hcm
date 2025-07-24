package tech.stl.hcm.jobrequisition.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import java.util.List;
import java.util.Optional;

public interface JobRequisitionService {
    JobRequisitionDTO createJobRequisition(JobRequisitionCreateDTO createDTO);
    JobRequisitionDTO updateJobRequisition(JobRequisitionDTO dto);
    void deleteJobRequisition(Integer jobRequisitionId);
    Optional<JobRequisitionDTO> getJobRequisitionById(Integer jobRequisitionId);
    List<JobRequisitionDTO> getAllJobRequisitions();
    Page<JobRequisitionDTO> getAllJobRequisitionsPaginated(Pageable pageable);
} 