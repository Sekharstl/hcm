package tech.stl.hcm.jobrequisition.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.stl.hcm.jobrequisition.service.JobRequisitionStatusService;
import tech.stl.hcm.common.dto.JobRequisitionStatusDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionStatusCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import tech.stl.hcm.common.mapper.JobRequisitionStatusMapper;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@TopicListener(
        topic = "${jobrequisitionstatus.kafka.topic}",
        groupId = "${jobrequisitionstatus.kafka.group-id}",
        valueType = Map.class,
        enableProperty = "jobrequisitionstatus.kafka.enable"
)
public class CreateJobRequisitionStatusConsumer extends BaseTransactionConsumer<JobRequisitionStatusDTO> {
    private final JobRequisitionStatusService jobRequisitionStatusService;
    
    public CreateJobRequisitionStatusConsumer(JobRequisitionStatusService jobRequisitionStatusService, 
                                           ObjectMapper objectMapper, 
                                           TransactionUpdateService transactionUpdateService) {
        super(objectMapper, transactionUpdateService);
        this.jobRequisitionStatusService = jobRequisitionStatusService;
    }

    @Override
    protected Class<JobRequisitionStatusDTO> getEntityType() {
        return JobRequisitionStatusDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Job Requisition Status";
    }

    @Override
    protected Object processEntity(JobRequisitionStatusDTO status, UUID transactionId) {
        JobRequisitionStatusCreateDTO createDTO = JobRequisitionStatusMapper.toCreateDTO(status);
        return jobRequisitionStatusService.createJobRequisitionStatus(createDTO);
    }

    @Override
    protected boolean isSuccess(Object result) {
        return result != null;
    }

    @Override
    protected UUID getEntityId(Object result) {
        // Return the created entity ID if available
        return null;
    }
} 