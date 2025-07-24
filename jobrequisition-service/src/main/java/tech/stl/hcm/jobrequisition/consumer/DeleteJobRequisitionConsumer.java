package tech.stl.hcm.jobrequisition.consumer;

import org.springframework.stereotype.Component;
import tech.stl.hcm.jobrequisition.service.JobRequisitionService;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.service.BaseTransactionConsumer;
import tech.stl.hcm.common.service.TransactionUpdateService;
import tech.stl.hcm.message.broker.consumer.TopicListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@TopicListener(
        topic = "${jobrequisition.kafka.topic.delete}",
        groupId = "${jobrequisition.kafka.group-id}",
        valueType = java.util.Map.class,
        enableProperty = "jobrequisition.kafka.enable"
)
public class DeleteJobRequisitionConsumer extends BaseTransactionConsumer<JobRequisitionDTO> {
    private final JobRequisitionService jobRequisitionService;

    public DeleteJobRequisitionConsumer(ObjectMapper objectMapper, TransactionUpdateService transactionUpdateService, JobRequisitionService jobRequisitionService) {
        super(objectMapper, transactionUpdateService);
        this.jobRequisitionService = jobRequisitionService;
    }

    @Override
    protected Class<JobRequisitionDTO> getEntityType() {
        return JobRequisitionDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "Job Requisition Delete";
    }

    @Override
    protected Object processEntity(JobRequisitionDTO jobRequisition, UUID transactionId) {
        jobRequisitionService.deleteJobRequisition(jobRequisition.getJobRequisitionId());
        return jobRequisition;
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
        // Job Requisition ID is Integer, so we return null for UUID
        return null;
    }
} 