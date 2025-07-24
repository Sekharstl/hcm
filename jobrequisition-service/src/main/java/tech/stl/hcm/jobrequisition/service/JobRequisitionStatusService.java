package tech.stl.hcm.jobrequisition.service;

import tech.stl.hcm.common.dto.JobRequisitionStatusDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionStatusCreateDTO;
import java.util.List;
import java.util.Optional;

public interface JobRequisitionStatusService {
    JobRequisitionStatusDTO createJobRequisitionStatus(JobRequisitionStatusCreateDTO statusCreateDTO);
    JobRequisitionStatusDTO updateJobRequisitionStatus(JobRequisitionStatusDTO statusDTO);
    void deleteJobRequisitionStatus(Integer statusId);
    Optional<JobRequisitionStatusDTO> getJobRequisitionStatusById(Integer statusId);
    List<JobRequisitionStatusDTO> getAllJobRequisitionStatuses();
} 