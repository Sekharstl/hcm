package tech.stl.hcm.jobrequisition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.JobRequisitionStatus;
import tech.stl.hcm.common.db.repositories.JobRequisitionStatusRepository;
import tech.stl.hcm.common.dto.JobRequisitionStatusDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionStatusCreateDTO;
import tech.stl.hcm.common.mapper.JobRequisitionStatusMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRequisitionStatusServiceImpl implements JobRequisitionStatusService {
    private final JobRequisitionStatusRepository jobRequisitionStatusRepository;

    @Override
    @Transactional
    public JobRequisitionStatusDTO createJobRequisitionStatus(JobRequisitionStatusCreateDTO statusCreateDTO) {
        JobRequisitionStatus entity = JobRequisitionStatusMapper.toEntity(statusCreateDTO);
        return JobRequisitionStatusMapper.toDTO(jobRequisitionStatusRepository.save(entity));
    }

    @Override
    @Transactional
    public JobRequisitionStatusDTO updateJobRequisitionStatus(JobRequisitionStatusDTO statusDTO) {
        Optional<JobRequisitionStatus> optional = jobRequisitionStatusRepository.findById(statusDTO.getStatusId());
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("JobRequisitionStatus not found");
        }
        JobRequisitionStatus entity = optional.get();
        entity.setName(statusDTO.getName());
        entity.setUpdatedAt(statusDTO.getUpdatedAt());
        entity.setUpdatedBy(statusDTO.getUpdatedBy());
        return JobRequisitionStatusMapper.toDTO(jobRequisitionStatusRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteJobRequisitionStatus(Integer statusId) {
        jobRequisitionStatusRepository.deleteById(statusId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobRequisitionStatusDTO> getJobRequisitionStatusById(Integer statusId) {
        return jobRequisitionStatusRepository.findById(statusId).map(JobRequisitionStatusMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobRequisitionStatusDTO> getAllJobRequisitionStatuses() {
        return jobRequisitionStatusRepository.findAll().stream().map(JobRequisitionStatusMapper::toDTO).collect(Collectors.toList());
    }
} 