package tech.stl.hcm.jobrequisition.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.jobrequisition.service.JobRequisitionService;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import tech.stl.hcm.common.mapper.JobRequisitionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${jobrequisition.kafka.topic}",
        groupId = "${jobrequisition.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "jobrequisition.kafka.enable"
)
public class CreateJobRequisitionConsumer extends BaseTransactionConsumer<JobRequisitionCreateDTO> {
    private final JobRequisitionService jobRequisitionService;

    public CreateJobRequisitionConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, JobRequisitionService jobRequisitionService) {
        super(objectMapper, transactionUpdateService);
        this.jobRequisitionService = jobRequisitionService;
    }

    @Override
    protected Class<JobRequisitionCreateDTO> getEntityType() {
        return JobRequisitionCreateDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Job Requisition";
    }

    @Override
    protected Object processEntity(JobRequisitionCreateDTO jobRequisition, UUID transactionId) {
        return jobRequisitionService.createJobRequisition(jobRequisition);
    }

    @Override
    protected boolean isSuccess(Object result) {
        if (result instanceof JobRequisitionDTO) {
            JobRequisitionDTO jobRequisition = (JobRequisitionDTO) result;
            return jobRequisition != null && jobRequisition.getJobRequisitionId() != null;
        }
        return false;
    }

    @Override
    protected UUID getEntityId(Object result) {
        if (result instanceof JobRequisitionDTO) {
            JobRequisitionDTO jobRequisition = (JobRequisitionDTO) result;
            // Job Requisition ID is Integer, so we return null for UUID
            // The transaction will still be tracked but without entity ID
            return null;
        }
        return null;
    }
} 