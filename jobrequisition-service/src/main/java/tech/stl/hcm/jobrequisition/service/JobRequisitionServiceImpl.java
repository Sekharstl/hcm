package tech.stl.hcm.jobrequisition.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.stl.hcm.common.db.entities.JobRequisition;
import tech.stl.hcm.common.db.repositories.JobRequisitionRepository;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.common.mapper.JobRequisitionMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRequisitionServiceImpl implements JobRequisitionService {
    private final JobRequisitionRepository jobRequisitionRepository;

    @Override
    @Transactional
    public JobRequisitionDTO createJobRequisition(JobRequisitionCreateDTO createDTO) {
        JobRequisition entity = JobRequisitionMapper.toEntity(createDTO);
        return JobRequisitionMapper.toDTO(jobRequisitionRepository.save(entity));
    }

    @Override
    @Transactional
    public JobRequisitionDTO updateJobRequisition(JobRequisitionDTO dto) {
        Optional<JobRequisition> optional = jobRequisitionRepository.findById(dto.getJobRequisitionId());
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Job Requisition not found");
        }
        JobRequisition entity = optional.get();
        // Set fields to update (example: title, statusId, etc.)
        entity.setTitle(dto.getTitle());
        entity.setStatusId(dto.getStatusId());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setUpdatedBy(dto.getUpdatedBy());
        // Add other updatable fields as needed
        return JobRequisitionMapper.toDTO(jobRequisitionRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteJobRequisition(Integer jobRequisitionId) {
        jobRequisitionRepository.deleteById(jobRequisitionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobRequisitionDTO> getJobRequisitionById(Integer jobRequisitionId) {
        return jobRequisitionRepository.findById(jobRequisitionId).map(JobRequisitionMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobRequisitionDTO> getAllJobRequisitions() {
        return jobRequisitionRepository.findAll().stream().map(JobRequisitionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobRequisitionDTO> getAllJobRequisitionsPaginated(Pageable pageable) {
        return jobRequisitionRepository.findAll(pageable)
                .map(JobRequisitionMapper::toDTO);
    }
} 